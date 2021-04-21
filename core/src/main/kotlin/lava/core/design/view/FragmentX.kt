package lava.core.design.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import lava.core.design.view.struct.*

/**
 * Created by svc on 2021/3/2
 */

abstract class FragmentX : Fragment(), StructHost {
    protected lateinit var binding: ViewDataBinding
    protected lateinit var struct: StructView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getStructView().build(this)
    }

    override fun getStructView(): StructView {
        return DecorView() + LoadingView()
    }

    override fun onSetup(contentView: View, binding: ViewDataBinding) {
        binding.lifecycleOwner = this
        this.binding = binding
        initView(binding)
        initEvent()
    }

    protected open fun initView(binding: ViewDataBinding) {}

    protected open fun initEvent() {}
}