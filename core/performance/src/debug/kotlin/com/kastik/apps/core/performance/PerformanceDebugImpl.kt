package com.kastik.apps.core.performance

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PerformanceDebugImpl @Inject constructor(

) : Performance {
    override fun <T> trace(name: String, attributes: Map<String, String>, block: () -> T): T {
        return block()
    }
}