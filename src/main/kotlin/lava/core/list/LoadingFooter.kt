package lava.core.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import lava.core.type.Block

enum class LoadMoreState {
    READY, LOADING, ERROR, DONE
}

interface LoadingFooter {
    fun asView(): View

    fun attach(recyclerView: RecyclerView)

    fun onLoad(block: Block)
}