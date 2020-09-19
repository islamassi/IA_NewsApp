package com.islamassi.latestnews.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.islamassi.latestnews.Constants
import org.jetbrains.annotations.NotNull
import java.io.Serializable

/**
 * Article data class
 */
@Entity
data class Article(
	@PrimaryKey
	@SerializedName("title")
	@NonNull
	var title: String = "",

	@SerializedName("description") var description: String?,
	@SerializedName("author") var author: String? = "CNN",
	@SerializedName("url") var url: String?,
	@SerializedName("content") var content: String?,
	@SerializedName("urlToImage") var urlToImage: String?,
	@SerializedName("publishedAt") var publishedAt: String?
){
	fun getTitleTransition():String = getTransition(Constants.TITLE_PREFIX_TRANS)
	fun getDescTransition():String = getTransition(Constants.DESCRIPTION_PREFIX_TRANS)
	fun getImageTransition():String = getTransition(Constants.IMAGE_PREFIX_TRANS)
	fun getDateTransition():String = getTransition(Constants.DATE_PREFIX_TRANS)
	private fun getTransition(prefix:String):String = prefix + title
}
