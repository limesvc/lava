@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package lava.core.util

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