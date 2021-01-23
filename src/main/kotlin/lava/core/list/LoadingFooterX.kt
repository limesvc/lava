package lava.core.list

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import lava.core.ext.am
import lava.core.type.Block

abstract class LoadingFooterX @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LoadingFooter {
    private var scrollListener: RecyclerView.OnScrollListener? = null

    protected var state = LoadMoreState.READY

    private var loadMoreLsn: Block? = null

    protected var recyclerView: RecyclerView? = null

    override fun asView() = this

    override fun attach(recyclerView: RecyclerView) {
        recyclerView.adapter.am<ListAdapterM<*>> {
            it.addFooter(this)
        }
        this.recyclerView = recyclerView
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && state == LoadMoreState.READY
                    && loadMoreLsn != null
                ) {
                    updateState(LoadMoreState.LOADING)
                }
            }
        }
        this.scrollListener = scrollListener
        recyclerView.addOnScrollListener(scrollListener)
    }

    protected open fun detach() {
        state = LoadMoreState.READY
        scrollListener?.also {
            recyclerView?.removeOnScrollListener(it)
        }
        recyclerView?.adapter.am<ListAdapterM<*>> {
            it.removeFooter(this)
        }
        recyclerView = null
        loadMoreLsn = null
    }

    fun updateState(state: LoadMoreState) {
        this.state = state
        when (state) {
            LoadMoreState.READY -> onReady()
            LoadMoreState.ERROR -> onError()
            LoadMoreState.DONE -> onDone()
            LoadMoreState.LOADING -> onLoading()
        }
    }

    override fun onLoad(block: Block) {
        loadMoreLsn = block
    }

    protected open fun onReady() {}

    protected open fun onLoading() {
        loadMoreLsn?.invoke()
    }

    protected open fun onError() {}

    protected open fun onDone() {
        detach()
    }
}