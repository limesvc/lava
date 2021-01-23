package lava.core.list

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class ListAdapterX<DATA, BINDING : ViewDataBinding> :
    ListAdapter<DATA, ViewHolderX<BINDING>>() {
    private val mData = mutableListOf<DATA>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderX<BINDING> {
        val binding = binding(viewType, parent)
        return ViewHolderX(binding)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: ViewHolderX<BINDING>, position: Int) {
        onBindView(position, getItem(position), holder.binding)
    }

    abstract fun binding(viewType: Int, parent: ViewGroup): BINDING

    abstract fun onBindView(position: Int, data: DATA, binding: BINDING)

    fun replaceAll(data: List<DATA>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
    }

    fun insert(data: DATA, position: Int) {
        mData.add(position, data)
        notifyItemInserted(position)
    }

    fun refresh(data: DATA, position: Int) {
        mData[position] = data
        notifyItemChanged(position)
    }
}

class ViewHolderX<BINDING : ViewDataBinding>(val binding: BINDING) :
    RecyclerView.ViewHolder(binding.root)