package com.kastik.benchmark.apps.settings

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.textAsString

fun MacrobenchmarkScope.navigateToSettings() {
    onElement(timeoutMs = 2000) { contentDescription == "Settings" }.click()
}

fun MacrobenchmarkScope.toggleSortingOptions() {
    onElement(timeoutMs = 2000) { textAsString() == "Descending" }.click()
    onElement(timeoutMs = 2000) { textAsString() == "Ascending" }.click()
    onElement(timeoutMs = 2000) { textAsString() == "Priority" }.click()
}

fun MacrobenchmarkScope.toggleFabFilters() {
    onElement(timeoutMs = 2000) { textAsString() == "For You" }.click()
    onElement(timeoutMs = 2000) { textAsString() == "For You" }.click()
}


fun MacrobenchmarkScope.toggleSearchFieldOptions() {
    onElement(timeoutMs = 2000) { textAsString() == "Body" }.click()
    onElement(timeoutMs = 2000) { textAsString() == "Both" }.click()
    onElement(timeoutMs = 2000) { textAsString() == "Title" }.click()
}

fun MacrobenchmarkScope.toggleThemeOptions() {
    onElement(timeoutMs = 2000) { textAsString() == "Light" }.click()
    onElement(timeoutMs = 2000) { textAsString() == "Dark" }.click()
    onElement(timeoutMs = 2000) { textAsString() == "System" }.click()
}

fun MacrobenchmarkScope.toggleDynamicTheme() {
    repeat(2) {
        onElement(timeoutMs = 2000) { textAsString() == "Dynamic color" }.click()
    }
}