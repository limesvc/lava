package lava.core.list

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import lava.core.util.LPUtil
import lava.core.util.MATCH

class LoadingFooterM @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LoadingFooterX(context, attrs, defStyleAttr) {
    init {
        val progressDrawable = CircularProgressDrawable(context)
        val imageView = ImageView(context)
        imageView.setImageDrawable(progressDrawable)
        addView(imageView, LPUtil.viewGroup(MATCH, 36))

        setOnClickListener {
            if (state == LoadMoreState.ERROR) {
                updateState(LoadMoreState.LOADING)
            }
        }
    }

    override fun asView() = this

    override fun onError() {

    }
}