package com.kastik.benchmark.apps.settings

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString

fun MacrobenchmarkScope.navigateToSettings() {
    onElement { contentDescription == "Settings" }.click()
}

fun MacrobenchmarkScope.toggleSortingOptions() {
    onElement { textAsString() == "Descending" }.click()
    onElement { textAsString() == "Ascending" }.click()
    onElement { textAsString() == "Priority" }.click()
}

fun UiAutomatorTestScope.toggleFabFilters() {
    onElement { textAsString() == "For You" }.click()
    onElement { textAsString() == "For You" }.click()
}


fun UiAutomatorTestScope.toggleSearchFieldOptions() {
    onElement { textAsString() == "Body" }.click()
    onElement { textAsString() == "Both" }.click()
    onElement { textAsString() == "Title" }.click()
}

fun UiAutomatorTestScope.toggleThemeOptions() {
    onElement { textAsString() == "Light" }.click()
    onElement { textAsString() == "Dark" }.click()
    onElement { textAsString() == "System" }.click()
}

fun UiAutomatorTestScope.toggleDynamicTheme() {
    repeat(2) {
        onElement { textAsString() == "Dynamic color" }.click()
    }
}