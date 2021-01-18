package lava.core.design.view.struct

import androidx.databinding.ViewDataBinding

/**
 * 模块化拼凑基类功能
 * 通过扩展方法提供封装调用
 * Created by svc on 2021/01/11
 */
interface Struct<T>

interface StructHost {
    fun getStructView(): StructView
    fun binding(): ViewDataBinding
    fun onSetup(binding: ViewDataBinding)
}

interface StructView {
    operator fun <T> get(struct: Struct<T>): T?

    operator fun plus(structView: StructView): StructView {
        return LinkStruct(structView, this)
    }

    fun build(host: StructHost) {
        this[DecorStruct]?.install(this, host)
    }
}

interface DecorStruct : StructView {
    companion object Key : Struct<DecorStruct>

    fun install(struct: StructView, host: StructHost)
}

interface ErrorStruct : StructView {
    companion object Key : Struct<ErrorStruct>
}

interface NavigateStruct : StructView {
    companion object Key : Struct<NavigateStruct>
}

interface LoadingStruct : StructView {
    companion object Key : Struct<LoadingStruct>
}

class LinkStruct(private val stuff: StructView, private val next: StructView) : StructView {

    override fun <T> get(struct: Struct<T>): T? {
        return stuff[struct] ?: next[struct]
    }
}