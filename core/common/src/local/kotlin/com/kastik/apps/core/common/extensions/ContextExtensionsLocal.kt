package com.kastik.apps.core.common.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri


fun Context.launchSignIn() {
    val url = "com.kastik.apps://auth?code=DEBUG_CODE"
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

