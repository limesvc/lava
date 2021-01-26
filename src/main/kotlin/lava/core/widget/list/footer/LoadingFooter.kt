package lava.core.widget.list.footer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import lava.core.widget.list.page.ILoadMore

interface LoadingFooter: ILoadMore {
    fun asView(): View

    fun attach(recyclerView: RecyclerView)
}