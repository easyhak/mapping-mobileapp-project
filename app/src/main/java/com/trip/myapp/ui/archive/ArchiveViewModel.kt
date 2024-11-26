package com.trip.myapp.ui.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.myapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArchiveViewModel @Inject constructor(

) : ViewModel() {

    private val _folders = MutableStateFlow(
        listOf(
            Folder(name = "전체", imageRes = R.drawable.category_sample_2)
        )
    )
    val folders: StateFlow<List<Folder>> = _folders.asStateFlow()

    fun addFolder(folderName: String) {
        viewModelScope.launch {
            _folders.value =
                _folders.value + Folder(name = folderName, imageRes = R.drawable.category_sample_1)
        }
    }
}
