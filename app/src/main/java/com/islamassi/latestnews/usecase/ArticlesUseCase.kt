package com.islamassi.latestnews.usecase

import androidx.lifecycle.LiveData
import com.islamassi.latestnews.api.Resource
import com.islamassi.latestnews.model.Article

/**
 * UseCase class for preparing articles in the needed format and structure
 */
interface ArticlesUseCase {
    fun getArticles(keyWord: String?): LiveData<Resource<List<Article>>>
}