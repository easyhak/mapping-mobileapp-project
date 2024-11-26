package com.trip.myapp.ui.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.myapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ArchiveViewModel : ViewModel() {

    private val _folders = MutableStateFlow(
        listOf(
            Folder(name = "전체", imageRes = R.drawable.category_sample_2, isDefault = true)
        )
    )
    val folders: StateFlow<List<Folder>> get() = _folders

    fun addFolder(folderName: String) {
        viewModelScope.launch {
            _folders.value =
                _folders.value + Folder(name = folderName, imageRes = R.drawable.category_sample_1)
        }
    }
}
