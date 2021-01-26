package lava.core.widget.list.footer

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import lava.core.net.LoadingState
import lava.core.util.LPUtil
import lava.core.util.MATCH

class LoadingFooterM @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LoadingFooterX(context, attrs, defStyleAttr) {
    private val imageView = ImageView(context)
    private val textView = TextView(context)

    init {
        val progressDrawable = CircularProgressDrawable(context)
        imageView.setImageDrawable(progressDrawable)
        addView(imageView, LPUtil.viewGroup(MATCH, 36))
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.BLACK)

        setOnClickListener {
            if (state == LoadingState.ERROR) {
                updateState(LoadingState.LOADING)
            }
        }
    }

    override fun asView() = this

    override fun onLoading() {
        super.onLoading()
        removeAllViews()
        addView(imageView, LPUtil.viewGroup())
    }

    override fun onError() {
        removeAllViews()
        addView(textView, LPUtil.viewGroup())
    }
}