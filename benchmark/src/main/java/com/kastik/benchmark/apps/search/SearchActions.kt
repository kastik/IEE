package com.kastik.benchmark.apps.search

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.type

fun MacrobenchmarkScope.navigateToSearchViaQuery() {
    onElement(timeoutMs = 2000) { contentDescription == "search_bar:input_field" }.click()
    val searchText = "Test"
    device.type(searchText)
    device.pressEnter()
    waitForStableInActiveWindow()
}