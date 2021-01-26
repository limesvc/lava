package lava.core.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import lava.core.widget.list.ListAdapterX
import lava.core.live.LiveList

const val PREFIX_RECYCLE = "android:recycle_"
const val RECYCLE_DATA = PREFIX_RECYCLE.plus("data")
const val RECYCLE_DATA_APPEND = PREFIX_RECYCLE.plus("data_append")
const val RECYCLE_ADAPTER = PREFIX_RECYCLE.plus("adapter")
const val RECYCLE_LAYOUT_MANAGER = PREFIX_RECYCLE.plus("layout_manager")
const val RECYCLE_DECORATION = PREFIX_RECYCLE.plus("decoration")

@BindingAdapter(
    value = [RECYCLE_DATA, RECYCLE_ADAPTER, RECYCLE_LAYOUT_MANAGER, RECYCLE_DECORATION],
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