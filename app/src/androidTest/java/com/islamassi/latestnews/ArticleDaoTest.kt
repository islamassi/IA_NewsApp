package com.islamassi.latestnews

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.espresso.base.MainThread
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.islamassi.latestnews.db.NewsDatabase
import com.islamassi.latestnews.model.Article
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.Assert.*
import org.junit.Rule
import org.junit.runners.JUnit4

@RunWith(AndroidJUnit4::class)
class ArticleDaoTest {

    private lateinit var database: NewsDatabase
    private lateinit var appContext: Context
    @Before
    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
                NewsDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    fun getArticleDummyList(): List<Article> {
        var article2 = Article(
            "Article2",
            "otherSource",
            "author2",
            "url",
            "no url string",
            "no url for thumbnail")

        var articleDummyList = ArrayList<Article>()
        articleDummyList.addAll(getArticleList())
        articleDummyList.add(article2)
        return articleDummyList
    }

    fun getArticleList(): List<Article> {
        var article = Article(
            "Article1",
            "desc",
            "auth",
            "test",
            "testImg",
            "no date"
        )

        var articleDummyList = ArrayList<Article>()
        articleDummyList.add(article)
        return articleDummyList
    }

    @Test
    fun testInsertAndRetrieve(){
        database.articleDao().insertArticles(getArticleDummyList())
        assertTrue(getArticleDummyList().containsAll(getValue(database.articleDao().getArticles())))
    }

    fun <T> getValue(liveData: LiveData<T>): T {
        val latch = CountDownLatch(1)
        val data = arrayOfNulls<Any>(1)
        appContext.mainExecutor.execute {
        val observer = object : Observer<T> {
            override fun onChanged(t: T?) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)
            }

        }
        liveData.observeForever(observer)
        }
        latch.await(2, TimeUnit.SECONDS)
        return data[0] as T
    }

    @Test
    fun testDelete() {
        database.articleDao().insertArticles(getArticleDummyList())
        database.articleDao().deleteAll()
        getValue(database.articleDao().getArticles())
        assertTrue(getValue(database.articleDao().getArticles()).isEmpty())
    }
}