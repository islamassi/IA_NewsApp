package com.islamassi.latestnews.viewholder

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import android.text.format.DateUtils
import android.view.View
import android.view.animation.AnimationSet
import androidx.appcompat.view.ViewPropertyAnimatorCompatSet
import androidx.recyclerview.widget.RecyclerView
import com.google.androidbrowserhelper.trusted.TwaLauncher
import com.islamassi.latestnews.databinding.ArticleViewBinding
import com.islamassi.latestnews.model.Article
import java.text.SimpleDateFormat
import java.util.*
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import androidx.core.view.ViewCompat
import androidx.core.view.updateMargins
import com.islamassi.latestnews.*

/**
 * RecyclerView ViewHolder that hold the article view and bind
 *
 * @param binding data binding for the article layout
 */
class ArticleViewHolder( val binding:ArticleViewBinding) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var article:Article
    private var animatorSet:AnimatorSet? = null
    private val animDuration = 1000L
    /**
     * bind article data to article layout
     * @param article article to be binded to the view
     */
    fun onBind(article:Article, shouldAnimate:Boolean){
        this.article = article
        animatorSet?.end()
        initTransitions()

        binding.article = article
        binding.articleImage.load(article.urlToImage, R.drawable.placeholder)
        if (shouldAnimate)
            animateCard()
        binding.title.setTextGoneOnEmpty(article.title)
        binding.description.setTextGoneOnEmpty(article.description)
        val date = article.publishedAt?.toDate("yyyy-MM-dd'T'HH:mm:ssX")
        date?.let {
            binding.publishDate.text =
                DateUtils.getRelativeTimeSpanString(
                    it.time,
                    Date().time,
                    DateUtils.MINUTE_IN_MILLIS
                )
        }
    }

    private fun initTransitions() {
        ViewCompat.setTransitionName(binding.articleImage, article.getImageTransition())
        ViewCompat.setTransitionName(binding.title, article.getTitleTransition())
        ViewCompat.setTransitionName(binding.description, article.getDescTransition())
        ViewCompat.setTransitionName(binding.publishDate, article.getDateTransition())
    }


    private fun animateCard() {
        binding.root.context.resources.apply {
            val dateTX:Float = getDimension(R.dimen.date_tx)
            val imageTY:Float = getDimension(R.dimen.image_ty)
            val cardTX:Float = getDimension(R.dimen.card_tx)
            val titleTX:Float = getDimension(R.dimen.title_tx)
            val cardMarginE:Int = (getDimension(R.dimen.card_margin)+ getDimension(R.dimen.card_elevation)).toInt()
            val cardMarginS:Int = getDimension(R.dimen.card_margin_start).toInt()

            val publishDateAnim = getPublishDateAnimSet(dateTX)

            val imageAnim = ObjectAnimator.ofFloat(binding.articleImage, "translationY", -imageTY, 0f)
                .setDuration(animDuration)
            val cardAnim =
                ObjectAnimator.ofFloat(binding.root, "translationX", -cardTX, 0f).setDuration(animDuration)
            val titleAnimation =
                ObjectAnimator.ofFloat(binding.title, "translationX", titleTX, 0f).setDuration(animDuration)
            val marginAnim = getMarginAnimator(cardMarginS, cardMarginE)

            animatorSet = AnimatorSet().apply {
                play(imageAnim).with(cardAnim).with(publishDateAnim).with(titleAnimation)
                    .with(marginAnim)
                start()
            }
        }
    }

    private fun getPublishDateAnimSet(dateTX: Float): Animator? =
        AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(binding.publishDate, "translationX", dateTX, 0f)
                    .setDuration(animDuration)
            )
            play(
                ObjectAnimator.ofFloat(binding.publishDate, "alpha", 0f, 1f)
                    .setDuration(animDuration/2)
            )
        }


    private fun getMarginAnimator(cardMarginS:Int, cardMarginE:Int): Animator? =
        ValueAnimator.ofInt(cardMarginS, cardMarginE).apply {
            duration = animDuration
            addUpdateListener {
                val params = binding.root.layoutParams as RecyclerView.LayoutParams
                params.updateMargins(
                    params.leftMargin,
                    it.animatedValue as Int,
                    params.rightMargin,
                    params.bottomMargin
                )
                binding.root.layoutParams = params
            }
        }
}