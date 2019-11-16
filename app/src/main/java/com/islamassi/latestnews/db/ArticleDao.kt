package com.islamassi.latestnews.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.islamassi.latestnews.model.Article
import android.provider.SyncStateContract.Helpers.update



@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articleList: List<Article>)

    @Query("DELETE FROM article")
    fun deleteAll()

    @Transaction
    fun replaceAll(articleList: List<Article>) {
        deleteAll()
        insertArticles(articleList)
    }
}