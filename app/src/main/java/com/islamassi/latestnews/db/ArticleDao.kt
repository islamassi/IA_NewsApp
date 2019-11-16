package com.islamassi.latestnews.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.islamassi.latestnews.model.Article
import android.provider.SyncStateContract.Helpers.update


/**
 * Dao interface for DB transactions on Article entity
 */
@Dao
interface ArticleDao {
    /**
     * Get all table records for articles
     *
     * @return fetched articles
     */
    @Query("SELECT * FROM article")
    fun getArticles(): LiveData<List<Article>>

    /**
     * insert articles to the table
     *
     * @param articleList articles to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articleList: List<Article>)

    /**
     * insert one article
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: Article)

    /**
     * Delete the whole table
     */
    @Query("DELETE FROM article")
    fun deleteAll()

    /**
     * DB transaction for replacing all records
     *
     * @param articleList articles to add
     */
    @Transaction
    fun replaceAll(articleList: List<Article>) {
        deleteAll()
        insertArticles(articleList)
    }
}