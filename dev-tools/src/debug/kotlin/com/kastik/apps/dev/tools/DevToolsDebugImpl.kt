package com.kastik.apps.dev.tools

import android.os.StrictMode
import com.kastik.apps.core.dev.tools.DevTools
import leakcanary.AppWatcher
import leakcanary.LeakCanary
import javax.inject.Inject

internal class DevToolsDebugImpl @Inject constructor(

) : DevTools {

    override fun setupStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build()
        )


        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build()
        )
    }

    override fun setupLeakCanary() {
        LeakCanary.config = LeakCanary.config.copy(
            retainedVisibleThreshold = 1
        )
    }

    override fun observeLeak(obj: Any, description: String) {
        AppWatcher.objectWatcher.expectWeaklyReachable(
            obj, description
        )
    }

}