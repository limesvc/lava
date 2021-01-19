package lava.core.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lava.core.ext.am
import lava.core.util.LPUtil

class ListViewX(context: Context, attrs: AttributeSet?) : SwipeRefreshLayout(context, attrs) {
    @Suppress
    private val recyclerView: RecyclerView = RecyclerView(context)

    private var footer: LoadingFooterX? = null

    init {
        addView(recyclerView, LPUtil.viewGroup())
    }

    fun setAdapter(adapter: ListAdapterM<*>) {
        recyclerView.adapter = adapter
        footer?.attachX(recyclerView)
    }

    fun initLoadMore(footer: LoadingFooterX? = null) {
        this.footer = footer
        footer?.attachX(recyclerView)
    }
}