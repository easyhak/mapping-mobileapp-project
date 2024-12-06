package com.trip.myapp.ui.archive.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ArchiveDetailScreen(
    viewModel: ArchiveDetailViewModel = hiltViewModel()
) {


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveDetailViewModel(id: String){
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Detail")
                }
            )
        }
    ){innerPadding ->
        Text("Detail", modifier = Modifier.padding(innerPadding))
    }
}
