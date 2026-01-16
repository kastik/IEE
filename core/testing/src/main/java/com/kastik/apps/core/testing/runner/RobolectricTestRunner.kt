package com.kastik.apps.core.testing.runner

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [34])
class RoboDatabaseTestRunner(testClass: Class<*>) : RobolectricTestRunner(testClass)