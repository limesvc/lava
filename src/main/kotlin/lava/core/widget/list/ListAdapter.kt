package lava.core.widget.list

import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class ListAdapter<DATA, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected val mData = LinkedList<DATA>()

    open fun setData(data: List<DATA>?) {
        mData.clear()
        if (data != null) {
            val success = mData.addAll(data)
        }
        notifyDataSetChanged()
    }

    open fun getItem(position: Int) = mData[position]

    override fun getItemCount() = mData.size
}