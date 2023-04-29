package lava.core

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.elvishew.xlog.XLog

/**
 * Created by svc on 2020/12/30
 */
lateinit var appContext: Context

open class AppX : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        appContext = this

        XLog.init()
    }
}