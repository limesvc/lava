package lava.core.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lava.core.ext.am
import lava.core.util.LPUtil

class ListViewX(context: Context, attrs: AttributeSet?) : SwipeRefreshLayout(context, attrs) {
    @Suppress
    private val recyclerView: RecyclerView = RecyclerView(context)

    private var loadMore = false
    private var addLoadMoreFooter = false
    private var footer: LoadingFooter? = null

    init {
        addView(recyclerView, LPUtil.viewGroup())
    }

    fun setAdapter(adapter: ListAdapterM<*>) {
        addLoadMoreFooter = recyclerView.adapter == adapter
        recyclerView.adapter = adapter

        if (loadMore && !addLoadMoreFooter) {
            adapter.addFooter(ensureFooter().asView())
        }
    }

    fun initLoadMore(footer: LoadingFooter? = null) {
        loadMore = true
        this.footer = footer
        recyclerView.adapter.am<ListAdapterM<*>> {
            it.addFooter(ensureFooter().asView())
            addLoadMoreFooter = true
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                }
            }
        })
    }

    private fun ensureFooter(): LoadingFooter {
        val ensure = footer ?: LoadingFooterM(context)
        footer = ensure
        return ensure
    }
}