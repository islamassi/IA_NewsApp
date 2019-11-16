package com.islamassi.latestnews.repo

import androidx.lifecycle.LiveData
import com.islamassi.latestnews.Constants
import com.islamassi.latestnews.api.ApiResponse
import com.islamassi.latestnews.api.Resource
import com.islamassi.latestnews.api.Webservice
import com.islamassi.latestnews.db.ArticleDao
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.model.NewsArticlesResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository responsible for providing articles
 *
 * It can be provided by DB or by a Retrofit request
 */
@Singleton
class ArticlesRepo @Inject constructor(
    private val webservice: Webservice, // Retrofit interface for server requests
    private val articleDao: ArticleDao // Dao for DB transactions
) {

    /**
     * getting news articles from locale database and then request a retrofit request and update the data
     *
     * @return a resource backed by both the database and network
     */
    fun getNewsArticles(keyWord: String?): LiveData<Resource<List<Article>>> {
        return object : NetworkBoundResource<List<Article>, NewsArticlesResponse>() {
            override fun saveCallResult(item: NewsArticlesResponse) {
                item.articles?.let {
                    articleDao.replaceAll(it)
                }
            }
            // always fetch from network in addition to loading from database
            override fun shouldFetch(data: List<Article>?): Boolean = true

            override fun loadFromDb(): LiveData<List<Article>> = articleDao.getArticles()

            override fun createCall(): LiveData<ApiResponse<NewsArticlesResponse>> =
                webservice.getArticles(Constants.API_KEY, keyWord)

        }.asLiveData()
    }
}