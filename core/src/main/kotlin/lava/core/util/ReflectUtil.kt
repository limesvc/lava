@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package lava.core.util

import android.app.Activity
import lava.core.compiler.Creator
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

fun getGenericClass(clazz: Class<*>): Type {
    val superclass: Type = clazz.genericSuperclass
    if (superclass is Class<*>) {
        throw RuntimeException("Missing type parameter.")
    }
    val typeList = superclass as ParameterizedType
    return typeList.actualTypeArguments[0]
}

fun inject(activity: Activity) {
    if (activity.javaClass.getAnnotation(Creator::class.java) != null) {
        val hostClass = Class.forName(activity.javaClass.name.plus("CreatorKT"))
        hostClass.getMethod("inject", hostClass).invoke(activity)
    }
}