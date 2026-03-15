package com.kastik.benchmark.apps.licence

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString

fun UiAutomatorTestScope.navigateToLicences() {
    onElement { contentDescription == "Settings" }.click()
    onElement { viewIdResourceName == "settings:content" }.scroll(Direction.DOWN, 1f)
    onElement { textAsString() == "Open source licenses" }.click()
}

fun UiAutomatorTestScope.scrollLicencesList() {
    val licenceList = onElement { viewIdResourceName == "licences:licence_list" }
    licenceList.scroll(Direction.DOWN, 1f)
    licenceList.scroll(Direction.UP, 1f)
}

fun UiAutomatorTestScope.openRandomLicence() {
    val licenceList = onElement { viewIdResourceName == "licences:licence_list" }
    licenceList.children.random().click()
    waitForStableInActiveWindow()
    device.pressBack()
}