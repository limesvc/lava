package lava.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lava.core.ext.am
import lava.core.ext.onNotNull
import lava.core.ext.removeFromParent
import lava.core.ext.replaceParent

/**
 * Created by svc on 2021/2/20
 */
class StickHeadViewX @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var recyclerView: RecyclerView

    private val stickerMap = mutableMapOf<Int, Sticker>()

    private var curSticker: Sticker? = null

    fun attach(recyclerView: RecyclerView, @IdRes vararg headerId: Int) {
        this.recyclerView = recyclerView
        headerId.forEach {
            stickerMap[it] = Sticker(-1, null, -1)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // 减少一层格式嵌套
                onScrolled(recyclerView, this)
            }
        })
    }

    private fun onScrolled(recyclerView: RecyclerView, listener: RecyclerView.OnScrollListener) {
        val first = recyclerView.firstVisibleItem() ?: return
        val next = recyclerView.firstCompletelyVisibleItem() ?: return

        val firstSticker = stickerMap[first.id]
        val nextSticker = stickerMap[next.id]
        if (firstSticker == null && nextSticker == null) return

        if (firstSticker != null) {
            if (firstSticker.holder == null) {
                val viewType = getViewType(listener, first) ?: return
                firstSticker.viewType = viewType
                firstSticker.holder?.itemView.replaceParent(this)
            }

            val hash = System.identityHashCode(first)
            if (firstSticker.hash != hash) {
                val holder = recyclerView.findContainingViewHolder(first) ?: return
                refreshHolder(firstSticker.holder, holder.layoutPosition)
                firstSticker.hash = hash
            }
        }

        if (nextSticker != null) {
            if (nextSticker.holder == null) {
                val viewType = getViewType(listener, first) ?: return
                nextSticker.viewType = viewType
            }
        }

        stick(firstSticker, nextSticker, next)
    }

    private fun stick(pre: Sticker?, next: Sticker?, nextView: View) {
        if (pre != null && next != null) {// 两个头部相邻，不需要粘性悬浮
            pre.holder?.itemView.removeFromParent()
            curSticker?.holder?.itemView.removeFromParent()
            curSticker = null
        } else if (pre != null && next == null && curSticker != pre) {
            curSticker?.holder?.itemView.removeFromParent()
            prepareHolder(pre)
            pre.holder?.itemView.replaceParent(this)
            curSticker = pre
        } else if (pre == null && next != null && curSticker != null) {
            curSticker?.holder?.itemView?.also {
                it.translationY = (nextView.top - it.height).toFloat()
            }
        }
    }

    private fun getViewType(listener: RecyclerView.OnScrollListener, child: View): Int? {
        recyclerView.adapter?.let { adapter ->
            var pos = RecyclerView.NO_POSITION
            child.layoutParams.am<RecyclerView.LayoutParams> {
                pos = it.absoluteAdapterPosition
            }

            if (pos == RecyclerView.NO_POSITION) {
                recyclerView.removeOnScrollListener(listener)
                return null
            }

            return adapter.getItemViewType(pos)
        }
        return null
    }

    private fun prepareHolder(sticker: Sticker) {
        if (sticker.holder == null) {
            recyclerView.adapter?.also { adapter ->
                val headHolder = adapter.createViewHolder(this@StickHeadViewX, sticker.viewType)
                headHolder.setIsRecyclable(false)
                sticker.holder = headHolder
            }
        }
    }

    private fun refreshHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        onNotNull(holder, recyclerView.adapter) { vh, adapter ->
            adapter.onBindViewHolder(vh, position)
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

data class Sticker(var viewType: Int = -1, var holder: RecyclerView.ViewHolder?, var hash: Int)