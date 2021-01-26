package lava.core.widget.list.footer

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import lava.core.ext.am
import lava.core.net.LoadingState
import lava.core.widget.list.ListAdapterM
import lava.core.type.Block

abstract class LoadingFooterX @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LoadingFooter {
    private var scrollListener: RecyclerView.OnScrollListener? = null

    protected var state = LoadingState.READY

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
                    && state == LoadingState.READY
                    && loadMoreLsn != null
                ) {
                    updateState(LoadingState.LOADING)
                }
            }
        }
        this.scrollListener = scrollListener
        recyclerView.addOnScrollListener(scrollListener)
    }

    protected open fun detach() {
        state = LoadingState.READY
        scrollListener?.also {
            recyclerView?.removeOnScrollListener(it)
        }
        recyclerView?.adapter.am<ListAdapterM<*>> {
            it.removeFooter(this)
        }
        recyclerView = null
        loadMoreLsn = null
    }

    override fun updateState(state: LoadingState) {
        this.state = state
        when (state) {
            LoadingState.READY -> onReady()
            LoadingState.ERROR -> onError()
            LoadingState.DONE -> onDone()
            LoadingState.LOADING -> onLoading()
        }
    }

    override fun onLoadMore(block: Block) {
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