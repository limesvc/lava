package lava.core.widget.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lava.core.net.LoadingState
import lava.core.type.Block
import lava.core.widget.list.footer.LoadingFooter
import lava.core.util.LPUtil
import lava.core.widget.list.page.IRefresh

class ListViewX(context: Context, attrs: AttributeSet?) : SwipeRefreshLayout(context, attrs),
    IRefresh {
    @Suppress
    val recyclerView: RecyclerView = RecyclerView(context)

    private var footer: LoadingFooter? = null

    init {
        addView(recyclerView, LPUtil.viewGroup())
    }

    var adapter: RecyclerView.Adapter<*>?
        get() = recyclerView.adapter
        set(value) {
            recyclerView.adapter = value
            footer?.attach(recyclerView)
        }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        recyclerView.addItemDecoration(decor, -1)
    }

    fun initLoadMore(footer: LoadingFooter? = null) {
        this.footer = footer
        footer?.attach(recyclerView)
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
    }

    override fun onRefresh(block: Block) {
        setOnRefreshListener(block)
    }

    override fun updateState(state: LoadingState) {
        when (state) {
            LoadingState.READY -> {
                isEnabled = true
                isRefreshing = false
            }
            LoadingState.LOADING -> {
                isRefreshing = true
            }
            LoadingState.ERROR -> {
                isEnabled = false
                isRefreshing = false
            }
            LoadingState.DONE -> Unit
        }
    }
}