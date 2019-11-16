package com.islamassi.latestnews.model

import com.google.gson.annotations.SerializedName

/**
 * data class for the response returned by the GET articles API
 */
data class NewsArticlesResponse (
    @SerializedName("status") var status: String?,
    @SerializedName("totalResults") var totalResults: Int?,
    @SerializedName("articles") var articles: List<Article>?
)