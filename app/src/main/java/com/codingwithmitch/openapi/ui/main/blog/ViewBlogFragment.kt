package com.codingwithmitch.openapi.ui.main.blog

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.model.BlogPost
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent
import com.codingwithmitch.openapi.ui.main.blog.viewModel.isAuthorOfBlogPost
import com.codingwithmitch.openapi.ui.main.blog.viewModel.setIsAuthorOfBlogPost
import com.codingwithmitch.openapi.util.DateUtils
import kotlinx.android.synthetic.main.fragment_view_blog.*

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
    }

    private fun checkIsAuthorOfBlogPost(){
        viewModel.setIsAuthorOfBlogPost(false) // reset
        viewModel.setStateEvent(BlogStateEvent.checkAuthorOfBlogPost())
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState->
            stateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.getContentIfNotHandled()?.let { viewSate->
                    viewSate.viewBlogFields.isAuthorOfBlogPost
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
        requestManager
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
            inflater.inflate(R.menu.edit_view_menu, menu)
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
        findNavController().navigate(R.id.action_viewBlogFragment_to_updateBlogFragment)
    }

}