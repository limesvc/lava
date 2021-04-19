package lava.core.live

import androidx.lifecycle.*
import lava.core.design.view.struct.StructView
import lava.core.ext.just

/**
 * Created by svc on 2021/1/28
 */
abstract class LiveBinding<T> {
    private var observeBinding = true
    private var live = MutableLiveData<T>()
    private var ownerProvider: (() -> LifecycleOwner?)? = null
    private var observer: Observer<in T>? = null

    fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (observeBinding) {
            observeBinding = false
            live.observe(owner, observer)

            owner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (Lifecycle.Event.ON_DESTROY == event) {
                        owner.lifecycle.removeObserver(this)
                    }
                    onLifeCycleChanged(source, event)
                }
            })
        }
    }

    fun observe(provider: () -> LifecycleOwner?, observer: Observer<in T>) {
        provider.invoke()?.apply {
            observe(this, observer)
        } ?: run {
            ownerProvider = provider
            this.observer = observer
        }
    }

    protected open fun onLifeCycleChanged(source: LifecycleOwner, event: Lifecycle.Event) {}

    protected fun postValue(value: T) {
        if (!live.hasObservers()) {
            val invoke = ownerProvider?.invoke()
            invoke?.just {
                observe(this, observer!!)
            }
        }
        live.postValue(value)
    }
}

class LiveStruct<T> : LiveBinding<T>() {
    fun attach(structView: StructView) {
    }
}