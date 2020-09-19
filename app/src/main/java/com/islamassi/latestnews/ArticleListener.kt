package com.islamassi.latestnews

import android.view.View
import android.widget.ImageView
import com.islamassi.latestnews.model.Article

interface ArticleListener {

    fun articleClicked(article:Article, image: ImageView, title: View, descrip: View, dateView: View);
}