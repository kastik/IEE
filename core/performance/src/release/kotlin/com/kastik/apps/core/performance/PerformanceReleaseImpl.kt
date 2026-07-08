package com.kastik.apps.core.performance

import com.google.firebase.perf.FirebasePerformance
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerformanceReleaseImpl @Inject constructor(
    private val perf: FirebasePerformance
) : Performance {
    override fun <T> trace(name: String, attributes: Map<String, String>, block: () -> T): T {
        val trace = perf.newTrace(name)

        attributes.forEach { (key, value) ->
            trace.putAttribute(key, value)
        }

        trace.start()
        return try {
            block()
        } finally {
            trace.stop()
        }
    }
}