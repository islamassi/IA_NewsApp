package com.islamassi.latestnews.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
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
	@SerializedName("author") var author: String?,
	@SerializedName("url") var url: String?,
	@SerializedName("content") var content: String?,
	@SerializedName("urlToImage") var urlToImage: String?,
	@SerializedName("publishedAt") var publishedAt: String?
): Serializable