package lava.core.widget.list

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class ListAdapterM<DATA> : ListAdapter<DATA, ViewHolderM>() {
    private val headerList = mutableListOf<View>()
    private val footerList = mutableListOf<View>()

    private val headOffset get() = headerList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            position in headOffset..headOffset + mData.size -> {
                getBindingType(position)
            }
            position < headOffset -> {
                -(position + 1)
            }
            else -> {
                -(position + 1)
            }
        }
    }

    open fun getBindingType(position: Int) = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderM {
        return when {
            viewType in -headOffset..0 -> {
                ViewHolderM(headerList[-viewType - 1])
            }
            viewType < 0 -> {
                ViewHolderM(footerList[-(viewType + 1 + headOffset + mData.size)])
            }
            else -> {
                val binding = binding(viewType, parent)
                ViewHolderM(viewType, binding)
            }
        }
    }

    override fun getItemCount() = headOffset + mData.size + footerList.size

    override fun onBindViewHolder(holder: ViewHolderM, position: Int) {
        holder.binding?.also {
            onBindView(holder.type, getItem(position), it)
        }
    }

    abstract fun binding(bindingType: Int, parent: ViewGroup): ViewDataBinding

    abstract fun onBindView(bindingType: Int, data: DATA, binding: ViewDataBinding)

    override fun getItem(position: Int) = mData[position - headOffset]

    fun addHeader(view: View) {
        if (!headerList.contains(view)) {
            headerList.add(view)
            notifyItemInserted(headerList.size - 1)
        }
    }

    fun removeHeader(view: View) {
        val index = headerList.indexOf(view)
        if (index >= 0) {
            headerList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun addFooter(view: View) {
        if (!footerList.contains(view)) {
            footerList.add(view)
            notifyItemInserted(itemCount - 1)
        }
    }

    fun removeFooter(view: View) {
        val index = footerList.indexOf(view)
        if (index >= 0) {
            footerList.removeAt(index)
            notifyItemRemoved(headOffset + mData.size + index)
        }
    }
}

class ViewHolderM(view: View) : RecyclerView.ViewHolder(view) {
    internal var binding: ViewDataBinding? = null
    internal var type: Int = 0

    constructor(type: Int, binding: ViewDataBinding) : this(binding.root) {
        this.type = type
        this.binding = binding
    }
}