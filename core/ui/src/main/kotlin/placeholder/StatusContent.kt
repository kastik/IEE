package com.kastik.apps.core.ui.placeholder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.component.IeePreview

@Composable
fun StatusContent(
    message: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        message()
        action?.let {
            it()
        }
    }
}

@Preview
@Composable
private fun StatusContentWithoutActionPreview() {
    IeePreview {
        StatusContent(
            message = {
                Text("You have been logged out")
            }
        )
    }
}

@Preview
@Composable
private fun StatusContentWithActionPreview() {
    IeePreview {
        StatusContent(
            message = {
                Text("There was an error while updating your profile")
            },
            action = {
                TextButton(onClick = {}) {
                    Text("Retry")
                }
            },
        )
    }
}
