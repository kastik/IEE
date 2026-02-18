package com.kastik.apps.core.common.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri


fun Context.launchSignIn() {
    val url = "com.kastik.apps://auth?code=DEBUG_CODE"
    val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addCategory(Intent.CATEGORY_BROWSABLE)
    }
    startActivity(intent)
}
