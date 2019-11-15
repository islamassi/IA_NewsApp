package com.islamassi.latestnews.api

import androidx.lifecycle.LiveData
import com.islamassi.latestnews.model.NewsArticlesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Webservice {
    @GET("/v2/top-headlines?country=gb")
    fun getArticles(@Query("apiKey") apiKey: String): LiveData<ApiResponse<NewsArticlesResponse>>
}