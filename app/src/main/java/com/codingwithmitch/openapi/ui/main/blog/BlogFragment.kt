package com.codingwithmitch.openapi.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.model.BlogPost
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent
import com.codingwithmitch.openapi.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_blog.*
import javax.inject.Inject

class BlogFragment :
    BaseBlogFragment(),
    BlogListAdapter.Interaction
{

    @Inject
    lateinit var requestManager: RequestManager

    private lateinit var recyclerAdapter: BlogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //goViewBlogFragment.setOnClickListener {
            //findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
        //}
        initRecyclerView()
        subscribeObserver()
        executeSearch()
    }

    private fun executeSearch(){
        viewModel.setQuery("")
        viewModel.setStateEvent(
            BlogStateEvent.BlogSearchEvent()
        )
    }

    private fun subscribeObserver(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState->
            if(dataState != null){
                stateChangeListener.onDataStateChange(dataState)
                dataState.data?.let {
                    it.data?.let { event ->
                        event.getContentIfNotHandled()?.let {
                            Log.d(TAG, "BlogFtagment, dataState: $it")
                            viewModel.setBlogListData(it.blogFields.blogList)
                        }
                    }
                }
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState->
            Log.d(TAG, "BlogFrament, viewState: ${viewState}")
            if(viewState != null){
                recyclerAdapter.submitList(
                    list = viewState.blogFields.blogList,
                    isQueryExhausted = true
                )
            }
        })
    }

    private fun initRecyclerView(){
        blog_post_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@BlogFragment.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingItemDecoration)
            addItemDecoration(topSpacingItemDecoration)

            recyclerAdapter = BlogListAdapter(
                requestManager = requestManager,
                interaction = this@BlogFragment
            )

            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if(lastPosition == recyclerAdapter.itemCount.minus(1)){
                        Log.d(TAG, "BlogFragment try to load next page...")
                    }
                }
            })
            adapter = recyclerAdapter
        }

    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        Log.d(TAG, "onItemSelected: position, BlogPost: $position, $item")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clear reference can leak memory
        blog_post_recyclerview.adapter = null
    }

}