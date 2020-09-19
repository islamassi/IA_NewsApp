package com.islamassi.latestnews.ui.custom

import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.constraintlayout.motion.widget.MotionLayout
import com.islamassi.latestnews.R

class ReadOptionsPopup(contentView: View?, width: Int, height: Int, focusable: Boolean) :
    PopupWindow(contentView, width, height, focusable) {
    private lateinit var motionLayout:MotionLayout
    init {
        animationStyle = R.style.SlidingDialogAnimation
        contentView?.setOnTouchListener(getTouchListener())
        motionLayout = contentView?.findViewById(R.id.containerMotionLayout)!!
    }

    fun show(viewToAttach: View){
        showAtLocation(viewToAttach, Gravity.TOP, 0, 0)
        motionLayout.postDelayed({ motionLayout.transitionToEnd() },200)
    }

    private fun getTouchListener():View.OnTouchListener=
        object : View.OnTouchListener {
            var downY = 0f
            override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downY = motionEvent.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (downY - motionEvent.rawY.toInt() > 200) {
                            dismiss()
                        }
                    }
                }
                return true
            }
        }

    companion object {
        fun newInstance(inflater: LayoutInflater): ReadOptionsPopup {
            val popupView: View = inflater.inflate(R.layout.layout_read_options, null)
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true
            return ReadOptionsPopup(popupView, width, height, focusable)
        }
    }

}