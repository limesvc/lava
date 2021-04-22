package lava.core.compiler

import com.squareup.kotlinpoet.ClassName

/**
 * Created by svc on 2021/4/14
 */
internal val Intent = ClassName("android.content", "Intent")
internal val Context = ClassName("android.content", "Context")
internal val Activity = ClassName("android.app", "Activity")
internal val Cast = ClassName("lava.core.ext", "am")