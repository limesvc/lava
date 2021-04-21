package lava.core.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import lava.core.live.LiveBinding

interface VoidBlock {
    fun invoke()
}

@BindingAdapter("android:test_binding")
fun <T> bindTest(view: TextView, live: LiveBinding<T>){

}