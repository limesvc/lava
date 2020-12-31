package lava.core

import android.content.Context
import androidx.multidex.MultiDexApplication

/**
 * Created by svc on 2020/12/30
 */
lateinit var appContext: Context

class AppExt: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}