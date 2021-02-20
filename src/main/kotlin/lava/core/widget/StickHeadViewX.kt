package lava.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lava.core.ext.am
import lava.core.ext.replaceParent

/**
 * Created by svc on 2021/2/20
 */
class StickHeadViewX @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var viewTypeMap = mutableMapOf<Int, Int>()
    private var holderMap = mutableMapOf<Int, RecyclerView.ViewHolder>()

    fun attach(recyclerView: RecyclerView, @IdRes vararg headerId: Int) {
        headerId.forEach {
            viewTypeMap[it] = -1
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // 减少一层嵌套
                onScrolled(recyclerView, this)
            }
        })
    }

    private fun onScrolled(recyclerView: RecyclerView, listener: RecyclerView.OnScrollListener) {
        val firstView = recyclerView.firstVisibleItem() ?: return
        val view = recyclerView.firstCompletelyVisibleItem() ?: return

        val viewType: Int = viewTypeMap[view.id] ?: recyclerView.adapter?.let { adapter ->
            var pos = RecyclerView.NO_POSITION
            view.layoutParams.am<RecyclerView.LayoutParams> {
                pos = it.absoluteAdapterPosition
            }

            if (pos == RecyclerView.NO_POSITION) {
                recyclerView.removeOnScrollListener(listener)
                return
            }

            adapter.getItemViewType(pos)
        } ?: return

        viewTypeMap[view.id] = viewType

        if (holderMap[viewType] == null) {
            recyclerView.adapter?.also { adapter ->
                val headHolder = adapter.createViewHolder(this@StickHeadViewX, viewType)
                headHolder.itemView.replaceParent(this)
                holderMap[viewType] = headHolder
            }
        }
    }

    private fun RecyclerView.firstCompletelyVisibleItem(): View? {
        layoutManager.am<LinearLayoutManager> {
            val pos = it.findFirstCompletelyVisibleItemPosition()
            if (pos != RecyclerView.NO_POSITION) {
                return it.findViewByPosition(pos)
            }
        }
        return null
    }

    private fun RecyclerView.firstVisibleItem(): View? {
        layoutManager.am<LinearLayoutManager> {
            val pos = it.findFirstVisibleItemPosition()
            if (pos != RecyclerView.NO_POSITION) {
                return it.findViewByPosition(pos)
            }
        }
        return null
    }
}