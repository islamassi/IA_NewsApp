package com.islamassi.latestnews

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.load(url: String) {
    Picasso.get()
        .load(url)
        .error(R.drawable.placeholder)
        .into(this)
}

fun TextView.setTextGoneOnEmpty(text: String?){
    if (!text.isNullOrEmpty()){
        this.text = text.trim()
        this.visibility = View.VISIBLE
    }else{
        this.text = ""
        this.visibility = View.GONE
    }
}

fun String.toDate(format:String):Date?{
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return try {
        dateFormat.parse(this)
    }catch (e: Throwable){
        null
    }
}