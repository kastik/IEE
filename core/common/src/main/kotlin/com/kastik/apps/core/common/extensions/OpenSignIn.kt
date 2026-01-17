package com.kastik.apps.core.common.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.launchSignIn() {
    //TODO Shouldn't hard code this, move it to gradle at some point
    val url = "https://login.it.teithe.gr/authorization?" +
            "client_id=690a9861468c9b767cabdc40" + "&response_type=code" +
            "&scope=announcements,profile" +
            "&redirect_uri=com.kastik.apps://auth"

    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}