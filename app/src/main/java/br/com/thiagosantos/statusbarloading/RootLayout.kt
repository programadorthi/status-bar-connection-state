package br.com.thiagosantos.statusbarloading

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class RootLayout : FrameLayout {

    private val linearLayout = LinearLayout(context).apply {
        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL)
        orientation = LinearLayout.HORIZONTAL
    }

    private val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleSmall ).apply {
        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL)
        isIndeterminate = true
    }

    private val textView = TextView(context).apply {
        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT)
        text = context.getText(R.string.connecting)
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13.0f)
        setTextColor(Color.WHITE)
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 0)
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        linearLayout.addView(progressBar)
        linearLayout.addView(textView)
        addView(linearLayout)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {

        layoutParams.height = insets.systemWindowInsetTop

        (0 until childCount)
                .map { getChildAt(it) }
                .forEach { it.dispatchApplyWindowInsets(insets) }

        return insets
    }

    fun hideStatus(window: Window) {
        if (y < 0) {
            return
        }
        val animator = SpringAnimation(linearLayout, DynamicAnimation.TRANSLATION_Y, -linearLayout.height.toFloat())
        animator.addEndListener { _, _, _, _ ->
            resetFullScreen(window)
        }
        animator.start()
    }

    fun showStatus(window: Window) {
        fullScreen(window)
        Handler().postDelayed({
            val animator = SpringAnimation(linearLayout, DynamicAnimation.TRANSLATION_Y, 0f)
            animator.start()
        }, 500L)
    }

    private fun fullScreen(window: Window) {
        window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LOW_PROFILE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#20111111")
        }
    }

    private fun resetFullScreen(window: Window) {
        val vis = window.decorView.systemUiVisibility
        val neg = View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()

        window.decorView.systemUiVisibility = vis and neg

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        }
    }

}