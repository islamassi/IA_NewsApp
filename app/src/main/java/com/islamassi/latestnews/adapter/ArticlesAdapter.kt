package com.islamassi.latestnews.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.islamassi.latestnews.ArticleListener
import com.islamassi.latestnews.databinding.ArticleViewBinding
import com.islamassi.latestnews.model.Article
import com.islamassi.latestnews.util.MyDiffCallback
import com.islamassi.latestnews.viewholder.ArticleViewHolder


/**
 * Adapter that binds Article items into a recyclerView list
 *
 * @link com.islamassi.latestnews.viewholder.ArticleViewHolder
 * @link com.islamassi.latestnews.model.Article
 */
class ArticlesAdapter constructor(
    private val articles : MutableList<Article>,
    private val listener:ArticleListener):
    RecyclerView.Adapter<ArticleViewHolder>() {
    var shouldAnimate = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(ArticleViewBinding.inflate(LayoutInflater.from(parent.context),parent, false ))

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        articles[position].apply {
            holder.itemView.setOnClickListener {
                listener.articleClicked(this, holder.binding.articleImage)
            }
            holder.onBind(this, shouldAnimate)
        }
    }

    /**
     * Handling new articles list and binding the items
     */
    fun notifyChange(newArticles: List<Article>){
        shouldAnimate = false
        /*articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()*/

        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(newArticles, this.articles))
        articles.clear()
        articles.addAll(newArticles)
        diffResult.dispatchUpdatesTo(this)
    }
}