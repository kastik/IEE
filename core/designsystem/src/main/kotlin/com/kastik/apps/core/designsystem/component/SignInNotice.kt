package com.kastik.apps.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme

@Composable
fun SignInNotice(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSignIn: () -> Unit,
) {

    AlertDialog(modifier = modifier, onDismissRequest = onDismiss, title = {
        Text("Sign In Required")
    }, text = {
        Text(
            "You’re currently browsing limited content. " + "Sign-ing to view all the announcements\n\n" + "Would you like to sign in now?",
        )
    }, confirmButton = {
        Button(
            onClick = onSignIn
        ) {
            Text("Sign-in")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Dismiss")
        }
    })

}

@Composable
fun NotificationNotice(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(modifier = modifier, onDismissRequest = onDismiss, title = {
        Text("Notification permission required")
    }, text = {
        Text(
            "To get notified about new announcements allow notifications. \n\n" + "" + "Would you like to enable them now?",
        )
    }, confirmButton = {
        Button(
            onClick = onConfirm
        ) {
            Text("Yes")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("No")
        }
    })

}

@Preview
@Composable
fun SignInNoticePreview() {
    AppsAboardTheme {
        SignInNotice(onDismiss = {}, onSignIn = {})
    }
}

@Preview
@Composable
fun NotificationNoticePreview() {
    AppsAboardTheme {
        NotificationNotice(onDismiss = {}, onConfirm = {})
    }
}