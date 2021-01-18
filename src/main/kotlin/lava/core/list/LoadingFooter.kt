package lava.core.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import lava.core.ext.am
import lava.core.util.LPUtil
import lava.core.util.MATCH

enum class LoadMoreState {
    LOADING, ERROR, DONE
}

interface LoadingFooter {
    fun asView(): View

    fun setState(state: LoadMoreState, text: String = "")
}

interface LoadMorePlugin {
    fun asView(): View

    fun attach(recyclerView: RecyclerView)
}

abstract class LoadingFooterX @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LoadMorePlugin {
    protected var state = LoadMoreState.LOADING

    override fun asView() = this

    fun attachX(recyclerView: RecyclerView) {
        recyclerView.adapter.am<ListAdapterM<*>> {
            it.addFooter(this)
        }
        attach(recyclerView)
    }

    override fun attach(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                }
            }
        })
    }

    fun updateState(state: LoadMoreState) {

    }
}

class LoadingFooterM @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LoadingFooter {
    init {
        val progressDrawable = CircularProgressDrawable(context)
        val imageView = ImageView(context)
        imageView.setImageDrawable(progressDrawable)
        addView(imageView, LPUtil.viewGroup(MATCH, 36))
    }

    override fun asView() = this
    override fun setState(state: LoadMoreState, text: String) {}
}