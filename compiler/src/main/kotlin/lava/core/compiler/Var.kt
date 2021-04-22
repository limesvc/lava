package lava.core.compiler

/**
 * Created by svc on 2021/4/14
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Var(val required: Boolean = true)