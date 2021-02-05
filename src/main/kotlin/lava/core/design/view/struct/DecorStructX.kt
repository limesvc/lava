package lava.core.design.view.struct

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import lava.core.ext.just
import lava.core.obj.UNCHECKED_CAST
import lava.core.util.LPUtil

abstract class DecorStructX : DecorStruct {

    override fun <T> get(struct: Struct<T>): T? {
        @Suppress(UNCHECKED_CAST)
        return if (DecorStruct == struct) this as T else null
    }

    override fun install(struct: StructView, host: StructHost):View {
        getContext(host).just {
            val decor = inflate(this)
            decor.layoutParams = LPUtil.viewGroup()
            decor.addView(host.binding().root)
            host.onSetup(decor, host.binding())
        }
        return View(host as Context)
    }

    private fun getContext(host: Any): Context? {
        return when (host) {
            is Context -> host
            is android.app.Fragment -> host.activity
            is Fragment -> host.requireContext()
            is View -> host.context
            else -> null
        }
    }

    protected open fun inflate(context: Context): ViewGroup {
        return LinearLayout(context)
    }
}