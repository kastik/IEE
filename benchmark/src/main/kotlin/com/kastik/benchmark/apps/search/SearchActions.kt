package com.kastik.benchmark.apps.search

import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.type

fun UiAutomatorTestScope.navigateToSearchViaQuery() {
    onElement(timeoutMs = 10000) { viewIdResourceName == "search_bar:input_field" }.click()
    waitForStableInActiveWindow()
    val searchText = "IEE"
    device.type(searchText)
    device.pressEnter()
}