package com.kastik.apps.core.dev.tools

interface DevTools {
    fun setupStrictMode()
    fun setupLeakCanary()
    fun observeLeak(obj: Any, description: String)
}