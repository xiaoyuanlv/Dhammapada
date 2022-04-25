package com.senlasy.dhammapada.utility

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

class Typewriter(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(
    context!!, attrs) {
    private var mText: CharSequence? = null
    private var mIndex: Int = 0
    private var mDelay: Long = 150 //Default 150ms delay
    public var type: String? = null

    private var listener: onFinishListener? = null

    interface onFinishListener {
        fun onFinishListener(view: TextView, text: String, type: String?)
    }

    public fun setAnimationFinishListener(listener: onFinishListener) {
        this.listener = listener
    }

    var characterAdder = Runnable {
        text = mText!!.subSequence(0, mIndex++)
        if (mIndex <= mText!!.length) {
            delayCaller()
        } else{
            Handler(Looper.myLooper()!!).postDelayed({
                hideTextView
            }, 1000);
        }
    }

    var hideTextView = Runnable {
        visibility = View.GONE
        listener!!.onFinishListener(this, mText.toString(), type)
    }

    fun delayCaller() {
        Handler(Looper.myLooper()!!).postDelayed({
            characterAdder
        }, mDelay);
    }


    fun animateText(text: CharSequence) {
        mText = text
        mIndex = 0

        setText("")
        Handler(Looper.myLooper()!!).postDelayed({
            characterAdder
        }, mDelay);
    }

    fun setCharacterDelay(millis: Long) {
        mDelay = millis
    }
}