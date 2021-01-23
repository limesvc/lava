package lava.core.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import lava.core.type.Block

enum class LoadingState {
    READY, LOADING, ERROR, DONE
}

interface LoadingFooter {
    fun asView(): View

    fun attach(recyclerView: RecyclerView)

    fun updateState(state: LoadingState)

    fun onLoad(block: Block)
}