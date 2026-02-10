package com.kastik.apps.core.common.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri


fun Context.launchSignIn() {
    val url = "https://login.iee.ihu.gr/authorization?" +
            "client_id=690a9861468c9b767cabdc40" + "&response_type=code" +
            "&scope=announcements,profile" +
            "&redirect_uri=com.kastik.apps://auth"
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}