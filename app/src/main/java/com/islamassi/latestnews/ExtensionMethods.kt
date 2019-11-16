package com.islamassi.latestnews

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.text.SimpleDateFormat
import java.util.*

/**
 * extension method for loading and downloading an image and showing it to an ImageView
 */
fun ImageView.load(url: String) {
    Picasso.get()
        .load(url)
        .error(R.drawable.placeholder)
        .into(this)
}

/**
 * Setting text to TextView and changing it is visibility to Gone if empty
 */
fun TextView.setTextGoneOnEmpty(text: String?){
    if (!text.isNullOrEmpty()){
        this.text = text.trim()
        this.visibility = View.VISIBLE
    }else{
        this.text = ""
        this.visibility = View.GONE
    }
}

/**
 * converting a string to a date
 *
 * @param format date format
 */
fun String.toDate(format:String):Date?{
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return try {
        dateFormat.parse(this)
    }catch (e: Throwable){
        null
    }
}