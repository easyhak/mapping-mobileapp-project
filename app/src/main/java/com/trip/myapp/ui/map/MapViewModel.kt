package com.trip.myapp.ui.map


import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.myapp.domain.usecase.AddPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

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

    fun savePost() {
        viewModelScope.launch {
            /* todo latitude longitude 넣을 수 있도록 하기 */
            /* todo event handling */
            try {
                addPostUseCase(
                    title = title.value,
                    content = content.value,
                    selectedImages = selectedImages.value,
                    startDate = startDate.value,
                    endDate = endDate.value,
                    pinColor = pinColor.value.toArgb().toLong(),
                    latitude = 0.0,
                    longitude = 0.0
                )
            } catch (e: Exception) {
                Log.e("MapViewModel", "savePost: $e")
            }

        }
    }
}