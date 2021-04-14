package com.codingwithmitch.openapi.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.model.BlogPost
import com.codingwithmitch.openapi.ui.AreYouSureCallback
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.UIMessage
import com.codingwithmitch.openapi.ui.UiMessageType
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent
import com.codingwithmitch.openapi.ui.main.blog.viewModel.*
import com.codingwithmitch.openapi.util.DateUtils
import com.codingwithmitch.openapi.util.SuccessHandling.Companion.SUCCESS_BLOG_DELETED
import kotlinx.android.synthetic.main.fragment_view_blog.*
import java.lang.Exception

class ViewBlogFragment : BaseBlogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
        checkIsAuthorOfBlogPost()
        stateChangeListener.expandAppbar()

        delete_button.setOnClickListener {
            confirmDeleteRequest()
        }
        Log.d(TAG," ViewBlogFragment onViewCreated")
    }

    private fun confirmDeleteRequest(){
        val callBack: AreYouSureCallback = object : AreYouSureCallback{
            override fun proceed() {
                deleteBlogPost()
            }

            override fun cancel() {
             // ignore
            }
        }

        uiCommunicationListener.onUIMessageReived(
            UIMessage(
                getString(R.string.are_you_sure_delete),
                UiMessageType.AreYouSureDialog(callBack)
            )
        )
    }

    private fun deleteBlogPost(){
        viewModel.setStateEvent(
            BlogStateEvent.DeleteBlogPostEvent()
        )
    }

    private fun checkIsAuthorOfBlogPost(){
        viewModel.setIsAuthorOfBlogPost(false) // reset
        viewModel.setStateEvent(BlogStateEvent.CheckAuthorOfBlogPost())
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState->
            if(dataState!=null){
                stateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.data?.getContentIfNotHandled()?.let { viewState->
                        viewModel.setIsAuthorOfBlogPost(
                            viewState.viewBlogFields.isAuthorOfBlogPost
                        )
                    }
                    data.response?.peekContent()?.let { response ->
                        if(response.message.equals(SUCCESS_BLOG_DELETED)){
                            viewModel.removeDeletedBlogPost()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState->
            viewState.viewBlogFields.blogPost?.let { blogPost ->
                setBlogProperties(blogPost)
            }
            if(viewState.viewBlogFields.isAuthorOfBlogPost){
                adapteViewToAuthorMode()
            }
        })
    }

    private fun adapteViewToAuthorMode() {
        activity?.invalidateOptionsMenu()
        delete_button.visibility = View.VISIBLE
    }

    private fun setBlogProperties(blogPost: BlogPost){
        dependencyProvider.getGlideRequestManager()
            .load(blogPost.image)
            .into(blog_image)

        blog_title.setText(blogPost.title)
        blog_author.setText(blogPost.username)
        blog_update_date.setText(DateUtils.convertLongToStringDate(
            blogPost.date_updated
        ))
        blog_body.setText(blogPost.body)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(viewModel.isAuthorOfBlogPost()){
            Log.d(TAG,"onCreateOptionsMenu is author")
            inflater.inflate(R.menu.edit_view_menu, menu)
        }else{
            Log.d(TAG,"onCreateOptionsMenu is not author")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(viewModel.isAuthorOfBlogPost()){
            when(item.itemId){
                R.id.edit ->{
                    navUpdateBlogFragment()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun navUpdateBlogFragment(){
        try {
            viewModel.setUpdatedBlogFields(
                viewModel.getBlogPost().title,
                viewModel.getBlogPost().body,
                viewModel.getBlogPost().image.toUri()
            )
            findNavController().navigate(R.id.action_viewBlogFragment_to_updateBlogFragment)
        }catch (e: Exception){
            Log.e(TAG, "Exception : ${e.message}")
        }
    }

}