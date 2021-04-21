package lava.core.binding

import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lava.core.ext.getLifeCycleOwner
import lava.core.widget.list.ListAdapterX
import lava.core.widget.list.ListViewX
import lava.core.widget.list.footer.LoadingFooter
import lava.core.widget.list.footer.LoadingFooterM
import lava.core.widget.list.page.LivePagerX

const val PREFIX_LIST = "android:list_"
const val LIST_DATA = PREFIX_LIST.plus("data")
const val LIST_DATA_APPEND = PREFIX_LIST.plus("data_append")
const val LIST_ADAPTER = PREFIX_LIST.plus("adapter")
const val LIST_LAYOUT_MANAGER = PREFIX_LIST.plus("layout_manager")
const val LIST_DECORATION = PREFIX_LIST.plus("decoration")
const val LIST_FOOTER = PREFIX_LIST.plus("footer")
const val LIST_LOAD_MORE = PREFIX_LIST.plus("load_more")
const val LIST_REFRESH = PREFIX_LIST.plus("refresh")
const val LIST_PAGER = PREFIX_LIST.plus("pager")

@BindingAdapter(
    value = [LIST_DATA,
        LIST_ADAPTER,
        LIST_LAYOUT_MANAGER,
        LIST_DECORATION,
        LIST_FOOTER,
        LIST_LOAD_MORE,
        LIST_REFRESH,
        LIST_PAGER],
    requireAll = false
)
fun <T, V : ListAdapterX<T, *>> bindListView(
    listView: ListViewX,
    data: List<T>?,
    adapter: V?,
    layoutManager: RecyclerView.LayoutManager?,
    decoration: RecyclerView.ItemDecoration?,
    footer: LoadingFooter?,
    loadMore: VoidBlock?,
    refresh: SwipeRefreshLayout.OnRefreshListener?,
    pager: LivePagerX<T>?
) {
    layoutManager?.also { listView.setLayoutManager(it) }
    if (listView.adapter != adapter && adapter != null) listView.adapter = adapter

    @Suppress("UNCHECKED_CAST")
    val curAdapter = (listView.adapter ?: adapter) as? ListAdapterX<T, *>
    if (data != null) {
        curAdapter?.setData(data)
    }

    if (decoration != null) listView.addItemDecoration(decoration)

    loadMore?.also {
        val plugin = footer ?: LoadingFooterM(listView.context)
        plugin.onLoadMore(loadMore::invoke)
        listView.initLoadMore(footer)
    }

    pager?.also {
        val plugin = footer ?: LoadingFooterM(listView.context)
        listView.initLoadMore(plugin)
        it.attach(plugin)
        it.attach(listView)
    }?.observe({
        listView.getLifeCycleOwner()
    }, Observer {
        curAdapter?.setData(it)
    })

    refresh?.also { listView.setOnRefreshListener(it) }
}