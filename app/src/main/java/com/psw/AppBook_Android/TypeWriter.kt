package com.psw.AppBook_Android

/**
 * Created by park on 2016-03-08.
 */
import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.util.AttributeSet

class TypeWriter : android.support.v7.widget.AppCompatTextView {

    private var mText: CharSequence? = null
    private var mIndex: Int = 0
    //private long mDelay = 150; //Default 150ms delay
    private var mDelay: Long = 90

    internal var prg: Progress? = null

    private val mHandler = Handler()
    private val characterAdder = object : Runnable {
        override fun run() {
            text = mText!!.subSequence(0, mIndex++)
            if (mIndex <= mText!!.length) {
                mHandler.postDelayed(this, mDelay)
            } else {
                if (prg == null) return
                // 완료되었다고 호출한다. 2016.03.08 박성완
                prg!!.onComplete()
            }
        }
    }

    constructor(context: Context) : super(context) {}

    override fun onDraw(canvas: Canvas) {
        if (isInEditMode) return
        super.onDraw(canvas)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    fun animateText(text: CharSequence) {
        mText = text
        mIndex = 0

        setText("")
        mHandler.removeCallbacks(characterAdder)
        mHandler.postDelayed(characterAdder, mDelay)
    }

    fun setCharacterDelay(millis: Long) {
        mDelay = millis
    }

    fun setOnComplete(prg: Progress) {
        this.prg = prg
    }

    // 외부에서 callback 처리를 위해 사용함 - 2016.03.08 박성완
    interface Progress {
        fun onComplete()
    }

}