package lava.core.ext

import android.view.LayoutInflater
import android.view.View

val View.inflater: LayoutInflater
    get() = LayoutInflater.from(context)