package lava.core.design.view.struct

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import lava.core.obj.UNCHECKED_CAST

abstract class DecorStructX : DecorStruct {
    override fun <T> get(struct: Struct<T>): T? {
        @Suppress(UNCHECKED_CAST)
        return if (DecorStruct == struct) this as T else null
    }

    protected fun getContext(host: Any): Context? {
        return when (host) {
            is Context -> host
            is android.app.Fragment -> host.activity
            is Fragment -> host.requireContext()
            is View -> host.context
            else -> null
        }
    }

}