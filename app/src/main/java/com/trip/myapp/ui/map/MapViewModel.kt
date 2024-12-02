package com.trip.myapp.ui.map


import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(

) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    // 선택된 이미지 URI 목록
    private val _selectedImages = MutableStateFlow<List<String>>(emptyList())
    val selectedImages = _selectedImages.asStateFlow()


    private val _startDate = MutableStateFlow("")
    val startDate = _startDate.asStateFlow()
    private val _endDate = MutableStateFlow("")
    val endDate = _endDate.asStateFlow()

    private val _pinColor = MutableStateFlow(Color(0xFF000000))
    val pinColor = _pinColor.asStateFlow()


    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateContent(content: String) {
        _content.value = content
    }


    fun addSelectedImages(images: List<String>) {
        _selectedImages.value = images
    }
    fun updateStartDate(startDate: String?) {
        _startDate.value = startDate ?: ""
    }

    fun updateEndDate(endDate: String?) {
        _endDate.value = endDate ?: ""
    }

    fun updatePinColor(pinColor: Color?) {
        _pinColor.value = pinColor ?: Color(0xFF000000)
    }
}