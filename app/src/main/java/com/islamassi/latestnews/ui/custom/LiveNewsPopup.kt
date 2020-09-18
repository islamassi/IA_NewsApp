package com.islamassi.latestnews.ui.custom

import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.islamassi.latestnews.R

class LiveNewsPopup(contentView: View?, width: Int, height: Int, focusable: Boolean) :
    PopupWindow(contentView, width, height, focusable) {
    private var mPosX = 0
    private var mPosY = 0
    private var title: TextView
    private var close: View

    init {
        animationStyle = R.style.FadeInDialogAnimation
        contentView?.setOnTouchListener(getTouchListener())
        title = contentView?.findViewById(R.id.title)!!
        close = contentView.findViewById(R.id.close)!!
        close.setOnClickListener { dismiss() }
    }

    fun show(titleTxt:String?, x:Int, y:Int, viewToAttach: View){
        titleTxt?.let {
            mPosX  = x
            mPosY = y
            title.text = it
            showAtLocation(viewToAttach, Gravity.CENTER, mPosX, mPosY)
        }
    }

    private fun getTouchListener():View.OnTouchListener =
        (object : View.OnTouchListener {
            private var dx = 0
            private var dy = 0
            override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dx = (mPosX - motionEvent.getRawX()).toInt()
                        dy = (mPosY - motionEvent.getRawY()).toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        mPosX = (motionEvent.rawX + dx).toInt()
                        mPosY = (motionEvent.rawY + dy).toInt()
                        update(mPosX, mPosY, -1, -1, true);
                    }
                }
                return true
            }
        })

    companion object {
        fun newInstance(inflater: LayoutInflater): LiveNewsPopup {
            val popupView: View = inflater.inflate(R.layout.layout_live_news, null)
            val width = inflater.context.resources.getDimension(R.dimen.live_layout_width)
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            return LiveNewsPopup(popupView, width.toInt(), height, false)
        }
    }

}