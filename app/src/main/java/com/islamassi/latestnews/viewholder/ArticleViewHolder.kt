package com.islamassi.latestnews.viewholder

import android.opengl.Visibility
import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.islamassi.latestnews.Constants
import com.islamassi.latestnews.databinding.ArticleViewBinding
import com.islamassi.latestnews.load
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.setTextGoneOnEmpty
import com.islamassi.latestnews.toDate
import java.text.SimpleDateFormat
import java.util.*

class ArticleViewHolder(private val binding:ArticleViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(article:Article){
        binding.article = article
        binding.articleImage.load(article.urlToImage?:Constants.PLACEHOLDER_IMAGE)
        binding.title.setTextGoneOnEmpty(article.title)
        binding.description.setTextGoneOnEmpty(article.description)
        val date = article.publishedAt?.toDate("yyyy-MM-dd'T'HH:mm:ssX")
        date?.let {
            binding.publishDate.text =
                DateUtils.getRelativeTimeSpanString(
                    it.time,
                    Date().time,
                    DateUtils.MINUTE_IN_MILLIS
                );
        }
    }

}