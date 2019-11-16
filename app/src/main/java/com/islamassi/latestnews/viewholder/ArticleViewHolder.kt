package com.islamassi.latestnews.viewholder

import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.androidbrowserhelper.trusted.TwaLauncher
import com.islamassi.latestnews.databinding.ArticleViewBinding
import com.islamassi.latestnews.model.Article
import java.text.SimpleDateFormat
import java.util.*
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import com.islamassi.latestnews.*


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

        article.url?.let { url: String ->
            binding.root.setOnClickListener {
                launchWithCustomColors(it, url)
            }
        }
    }

    /**
     * Launches a Trusted Web Activity where navigations to non-validate domains will open
     * in a Custom Tab where the toolbar color has been customized.
     *
     * @param view the source of the event invoking this method.
     */
    fun launchWithCustomColors(view: View, url:String) {
        if (url.isEmpty())
            return
        val builder = TrustedWebActivityIntentBuilder(Uri.parse(url))
            .setToolbarColor(view.resources.getColor(R.color.colorPrimary))
        TwaLauncher(view.context).launch(builder, null, null)
    }

}