package com.codingwithmitch.openapi.ui.main.blog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.*
import com.codingwithmitch.openapi.ui.main.blog.state.BLOG_VIEW_STATE_BUNDLE_KEY
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent
import com.codingwithmitch.openapi.ui.main.blog.state.BlogViewState
import com.codingwithmitch.openapi.ui.main.blog.viewModel.BlogViewModel
import com.codingwithmitch.openapi.ui.main.blog.viewModel.onBlogPostUpdateSuccess
import com.codingwithmitch.openapi.ui.main.blog.viewModel.setUpdatedBlogFields
import com.codingwithmitch.openapi.util.Constants
import com.codingwithmitch.openapi.util.ErrorHandling
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_update_blog.*
import kotlinx.android.synthetic.main.fragment_view_blog.blog_body
import kotlinx.android.synthetic.main.fragment_view_blog.blog_image
import kotlinx.android.synthetic.main.fragment_view_blog.blog_title
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UpdateBlogFragment @Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: RequestManager
) : BaseBlogFragment(R.layout.fragment_update_blog)
{

    val viewModel: BlogViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cancelActiveJobs()
        // restore state
        savedInstanceState?.let{ bundle->
            (bundle[BLOG_VIEW_STATE_BUNDLE_KEY] as BlogViewState?)?.let { viewState->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        viewState?.blogFields?.blogList = ArrayList()

        outState.putParcelable(
            BLOG_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun cancelActiveJobs() {
        viewModel.cancelActiveJobs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()

        image_container.setOnClickListener {
            if(stateChangeListener.isStoragePermissionGranted()){
                pickFromGallery()
            }
        }
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState->
            if(dataState!=null){
                stateChangeListener.onDataStateChange(dataState)

                dataState.data?.let { data ->
                    data.data?.getContentIfNotHandled()?.let { viewState->
                        viewState.viewBlogFields.blogPost?.let {blogPost ->
                            viewModel.onBlogPostUpdateSuccess(blogPost).let {
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }

            viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState->
                viewState.updateBlogFields.let { updateBlogFields ->
                    setBlogProperties(
                        updateBlogFields.updatedBlogTitle,
                        updateBlogFields.updatedBlogBody,
                        updateBlogFields.updatedImageUri
                    )
                }

            })
        })
    }

    private fun setBlogProperties(updatedBlogTitle: String?, updatedBlogBody: String?, updatedImageUri: Uri?) {
        requestManager
            .load(updatedImageUri)
            .into(blog_image)
        blog_title.setText(updatedBlogTitle)
        blog_body.setText(updatedBlogBody)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save ->{
                saveChanges()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }

    private fun launchImageCrop(uri: Uri?){
        context?.let {
            CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                Constants.GALLERY_REQUEST_CODE ->{
                    data?.data?.let { uri ->
                        activity?.let {
                            launchImageCrop(uri)
                        }
                    }?: showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ->{
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    Log.d(TAG, "CROP_IMAGE_ACTIVITY_REQUEST_CODE uri: $resultUri")
                    viewModel.setUpdatedBlogFields(
                        title = null,
                        body = null,
                        uri = resultUri
                    )
                }

                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE->{
                    showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
                }
            }
        }
    }


    private fun saveChanges() {
        var mutipartBody: MultipartBody.Part? = null
        viewModel.viewState.value?.updateBlogFields?.updatedImageUri?.let { imageUri->
            imageUri.path?.let { filePath->
                val imageFile = File(filePath)
                Log.d(TAG, "UpdateBlogFragment: imageFile: ${imageFile}")
                val requestBody = RequestBody.create(
                    MediaType.parse("image/*"),
                    imageFile
                )
                mutipartBody = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    requestBody
                )

            }
        }
        mutipartBody?.let {
            viewModel.setStateEvent(
                BlogStateEvent.UpdateBlogPostEvent(
                    blog_title.text.toString(),
                    blog_body.text.toString(),
                    it
                )
            )
            stateChangeListener.hideSoftKeyboard()
        }?: showErrorDialog(ErrorHandling.ERROR_MUST_SELECT_IMAGE)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setUpdatedBlogFields(
            uri = null,
            title = blog_title.text.toString(),
            body = blog_body.text.toString()
        )
    }

    private fun showErrorDialog(errorMessage: String){
        stateChangeListener.onDataStateChange(
            DataState(
                Event(StateError(Response(errorMessage, ResponseType.Dialog()))),
                Loading(false),
                Data(Event.dataEvent(null), null)
            )
        )
    }

}