package com.kastik.benchmark.apps.settings

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString

fun MacrobenchmarkScope.navigateToSettings() {
    onElement(timeoutMs = 10000) { contentDescription == "Settings" }.click()
}

fun MacrobenchmarkScope.toggleSortingOptions() {
    onElement(timeoutMs = 10000) { textAsString() == "Descending" }.click()
    onElement(timeoutMs = 10000) { textAsString() == "Ascending" }.click()
    onElement(timeoutMs = 10000) { textAsString() == "Priority" }.click()
}

fun UiAutomatorTestScope.toggleFabFilters() {
    onElement(timeoutMs = 10000) { textAsString() == "For You" }.click()
    onElement(timeoutMs = 10000) { textAsString() == "For You" }.click()
}


fun UiAutomatorTestScope.toggleSearchFieldOptions() {
    onElement(timeoutMs = 10000) { textAsString() == "Body" }.click()
    onElement(timeoutMs = 10000) { textAsString() == "Both" }.click()
    onElement(timeoutMs = 10000) { textAsString() == "Title" }.click()
}

fun UiAutomatorTestScope.toggleThemeOptions() {
    onElement(timeoutMs = 10000) { textAsString() == "Light" }.click()
    onElement(timeoutMs = 10000) { textAsString() == "Dark" }.click()
    onElement(timeoutMs = 10000) { textAsString() == "System" }.click()
}

fun UiAutomatorTestScope.toggleDynamicTheme() {
    repeat(2) {
        onElement(timeoutMs = 10000) { textAsString() == "Dynamic color" }.click()
    }
}