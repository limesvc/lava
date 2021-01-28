package lava.core.live

import androidx.lifecycle.*
import lava.core.design.view.struct.StructView

/**
 * Created by svc on 2021/1/28
 */
open class LiveBinding<T> : MutableLiveData<T>() {
    private var observeBinding = true

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)

        if (observeBinding) {
            observeBinding = false
            observe(owner, Observer {
                onChanged(it)
            })

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

    protected open fun onChanged(value: T) {}

    protected open fun onLifeCycleChanged(source: LifecycleOwner, event: Lifecycle.Event) {}
}

class LiveStruct<T> : LiveBinding<T>() {
    fun attach(structView: StructView){
    }
}