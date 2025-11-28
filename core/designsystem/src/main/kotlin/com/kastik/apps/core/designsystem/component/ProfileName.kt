package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileName(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(name, style = MaterialTheme.typography.headlineSmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Email, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(email, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview
@Composable
fun ProfileNamePreview() {
    ProfileName(
        name = "John Doe",
        email = "john.rutledge@examplepetstore.com"
    )
}

