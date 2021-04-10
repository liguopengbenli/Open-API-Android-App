package com.codingwithmitch.openapi.repository.main

import androidx.lifecycle.LiveData
import com.codingwithmitch.openapi.api.main.OpenApiMainService
import com.codingwithmitch.openapi.api.main.response.BlogCreateUpdateResponse
import com.codingwithmitch.openapi.model.AuthToken
import com.codingwithmitch.openapi.model.BlogPost
import com.codingwithmitch.openapi.persistence.BlogPostDao
import com.codingwithmitch.openapi.repository.JobManager
import com.codingwithmitch.openapi.repository.NetworkBoundResource
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.Response
import com.codingwithmitch.openapi.ui.ResponseType
import com.codingwithmitch.openapi.ui.main.create_blog.state.CreateBlogViewState
import com.codingwithmitch.openapi.util.AbsentLiveData
import com.codingwithmitch.openapi.util.DateUtils
import com.codingwithmitch.openapi.util.GenericApiResponse
import com.codingwithmitch.openapi.util.SuccessHandling.Companion.RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateBlogRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("CreateBlogRepository")
{
    private val TAG: String = "AppDebug"

    fun createNewBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): LiveData<DataState<CreateBlogViewState>>{
        return object : NetworkBoundResource<BlogCreateUpdateResponse, BlogPost, CreateBlogViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){
            override suspend fun createCacheRequestAndReturn() {
                // not use
            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<BlogCreateUpdateResponse>) {
                // check Paid membership
                if(!response.body.response.equals(RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER)){
                    val updateBlogPost = BlogPost(
                        response.body.pk,
                        response.body.title,
                        response.body.slug,
                        response.body.body,
                        response.body.image,
                        DateUtils.convertServerStringDateToLong(
                            response.body.date_updated
                        ),
                        response.body.username
                    )
                    updateLocalDb(updateBlogPost)
                }

                withContext(Main){
                    onCompleteJob(
                        DataState.data(
                            null,
                            Response(response.body.response, ResponseType.Dialog())
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogCreateUpdateResponse>> {
                return openApiMainService.createBlog(
                    "Token ${authToken.token}",
                    title,
                    body,
                    image
                )
            }

            override fun loadFromCache(): LiveData<CreateBlogViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: BlogPost?) {
                cacheObject?.let {
                    blogPostDao.insert(it)
                }
            }

            override fun setJob(job: Job) {
                addJob("createNewBlogPost", job)
            }
        }.asLiveData()
    }

}