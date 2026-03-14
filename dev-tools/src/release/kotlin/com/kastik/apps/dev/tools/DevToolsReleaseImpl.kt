package com.kastik.apps.dev.tools

import com.kastik.apps.core.dev.tools.DevTools
import javax.inject.Inject

internal class DevToolsReleaseImpl @Inject constructor(
) : DevTools {

    override fun setupStrictMode() = Unit

    override fun setupLeakCanary() = Unit

    override fun observeLeak(obj: Any, description: String) = Unit

}