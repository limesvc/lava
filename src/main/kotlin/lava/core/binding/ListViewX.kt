package lava.core.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lava.core.list.ListAdapterX
import lava.core.list.ListViewX
import lava.core.list.LoadingFooter
import lava.core.list.LoadingFooterM
import lava.core.live.LiveList

const val PREFIX_LIST = "android:list_"
const val LIST_DATA = PREFIX_LIST.plus("data")
const val LIST_DATA_APPEND = PREFIX_LIST.plus("data_append")
const val LIST_ADAPTER = PREFIX_LIST.plus("adapter")
const val LIST_LAYOUT_MANAGER = PREFIX_LIST.plus("layout_manager")
const val LIST_DECORATION = PREFIX_LIST.plus("decoration")
const val LIST_FOOTER = PREFIX_LIST.plus("footer")
const val LIST_LOAD_MORE = PREFIX_LIST.plus("load_more")
const val LIST_REFRESH = PREFIX_LIST.plus("refresh")

@BindingAdapter(
    value = [LIST_DATA,
        LIST_ADAPTER,
        LIST_LAYOUT_MANAGER,
        LIST_DECORATION,
        LIST_FOOTER,
        LIST_LOAD_MORE,
        LIST_REFRESH],
    requireAll = false
)
fun <T, V : ListAdapterX<T, *>> bindListView(
    listView: ListViewX,
    data: LiveList<T>?,
    adapter: V?,
    layoutManager: RecyclerView.LayoutManager?,
    decoration: RecyclerView.ItemDecoration?,
    footer: LoadingFooter?,
    loadMore: VoidBlock?,
    refresh: SwipeRefreshLayout.OnRefreshListener?
) {
    layoutManager?.also { listView.setLayoutManager(it) }
    if (listView.adapter != adapter && adapter != null) listView.adapter = adapter

    @Suppress("UNCHECKED_CAST")
    val curAdapter = (listView.adapter ?: adapter) as? ListAdapterX<T, *>
    curAdapter?.setData(data?.value)

    if (decoration != null) listView.addItemDecoration(decoration)

    loadMore?.also {
        val plugin = footer ?: LoadingFooterM(listView.context)
        plugin.onLoad(loadMore::invoke)
        listView.initLoadMore(footer)
    }

    refresh?.also { listView.setOnRefreshListener(it) }
}