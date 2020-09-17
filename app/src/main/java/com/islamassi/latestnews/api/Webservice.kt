package com.islamassi.latestnews.api

import androidx.lifecycle.LiveData
import com.islamassi.latestnews.model.NewsArticlesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * WebService interface contains Retrofit requests
 */
interface Webservice {

    /**
     * GET request for getting articles
     * @param apiKey newsApi key
     * @param keyWord search query
     */
    @GET("/v2/top-headlines?country=AT")
    fun getArticles(
        @Query("apiKey")
        apiKey: String,

        @Query("q")
        keyWord: String?
    ): LiveData<ApiResponse<NewsArticlesResponse>>
}