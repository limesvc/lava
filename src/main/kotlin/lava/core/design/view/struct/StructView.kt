package lava.core.design.view.struct

import android.content.Context
import android.view.DragEvent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import lava.core.net.LoadingState

/**
 * 模块化拼凑基类功能
 * 通过扩展方法提供封装调用
 * Created by svc on 2021/01/11
 */
interface Struct<T>

interface StructHost {
    fun getStructView(): StructView
    fun binding(): ViewDataBinding
    fun onSetup(contentView: View, binding: ViewDataBinding)
    fun <T> onViewStateChanged(state: StructState<T>): T
}

interface StructView {
    operator fun <T> get(struct: Struct<T>): T?

    operator fun plus(structView: StructView): StructView {
        return LinkStruct(structView, this)
    }

    fun build(host: StructHost): View {
        return this[DecorStruct]?.install(this, host)!!
    }

    fun onLifeCycleChanged(event: Lifecycle.Event) {}

    fun updateState(state: LoadingState) {}

    fun getView(context: Context): View
}

interface DecorStruct : StructView {
    companion object Key : Struct<DecorStruct>

    fun install(struct: StructView, host: StructHost): View

    fun onLifeCycleEvent(event: Lifecycle.Event) {}
}

interface ErrorStruct : StructView {
    companion object Key : Struct<ErrorStruct>
}

interface TitleStruct : StructView {
    companion object Key : Struct<TitleStruct>
}

interface LoadingStruct : StructView {
    companion object Key : Struct<LoadingStruct>
}

class LinkStruct(private val stuff: StructView, private val next: StructView) : StructView {

    override fun <T> get(struct: Struct<T>): T? {
        return stuff[struct] ?: next[struct]
    }

    override fun getView(context: Context): View {
        return stuff.getView(context)
    }
}