package com.islamassi.latestnews.usecase

import androidx.lifecycle.LiveData
import com.islamassi.latestnews.api.Resource
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.repo.ArticlesRepo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interactor class for preparing articles in the needed format and structure
 */
@Singleton
class ArticlesInteractor @Inject constructor(
    private val repo: ArticlesRepo
) : ArticlesUseCase {
    override fun getArticles(keyWord: String?): LiveData<Resource<List<Article>>> {
        return repo.getNewsArticles(keyWord)
    }
}