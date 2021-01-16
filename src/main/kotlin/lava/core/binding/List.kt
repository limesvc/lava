package lava.core.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import lava.core.list.ListAdapterX
import lava.core.live.LiveList

const val PREFIX_LIST = "android:list_"
const val LIST_DATA = PREFIX_LIST.plus("data")
const val LIST_DATA_APPEND = PREFIX_LIST.plus("data_append")
const val LIST_ADAPTER = PREFIX_LIST.plus("adapter")
const val LIST_LAYOUT_MANAGER = PREFIX_LIST.plus("layout_manager")
const val LIST_DECORATION = PREFIX_LIST.plus("decoration")

@BindingAdapter(
    value = [LIST_DATA, LIST_ADAPTER, LIST_LAYOUT_MANAGER, LIST_DECORATION],
    requireAll = false
)
fun <T, V : ListAdapterX<T, *>> bindList(
    recyclerView: RecyclerView,
    data: LiveList<T>?,
    adapter: V?,
    layoutManager: RecyclerView.LayoutManager?,
    decoration: RecyclerView.ItemDecoration?
) {
    layoutManager?.also { recyclerView.layoutManager = it }
    if (recyclerView.adapter == null && adapter != null) recyclerView.adapter = adapter

    @Suppress("UNCHECKED_CAST")
    val curAdapter = (recyclerView.adapter ?: adapter) as? ListAdapterX<T, *>
    curAdapter?.setData(data?.value)

    if (decoration != null) recyclerView.addItemDecoration(decoration)
}