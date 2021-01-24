package lava.core.list

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lava.core.util.LPUtil

class ListViewX(context: Context, attrs: AttributeSet?) : SwipeRefreshLayout(context, attrs) {
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
}