package lava.core.list

import androidx.recyclerview.widget.RecyclerView

abstract class ListAdapter<DATA, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    private val mData = mutableListOf<DATA>()

    open fun setData(data: List<DATA>?) {
        mData.clear()
        if (data != null) {
            mData.addAll(data)
        }
        notifyDataSetChanged()
    }

    open fun getItem(position: Int) = mData[position]
}