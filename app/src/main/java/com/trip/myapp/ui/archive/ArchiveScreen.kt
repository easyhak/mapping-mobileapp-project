package com.trip.myapp.ui.archive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ArchiveScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text("Archive Screen", style = MaterialTheme.typography.titleLarge)
    }
}




@Preview(showBackground = true)
@Composable
private fun PreviewArchiveScreen() {
    ArchiveScreen()
}
