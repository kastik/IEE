package com.kastik.apps.core.performance

interface Performance {
    fun <T> trace(name: String, attributes: Map<String, String>, block: () -> T): T
}
