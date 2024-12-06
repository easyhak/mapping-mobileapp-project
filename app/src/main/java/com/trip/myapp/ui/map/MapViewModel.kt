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

    private val _latitude = MutableStateFlow(37.5665)
    val latitude = _latitude.asStateFlow()

    private val _longitude = MutableStateFlow(126.9780)
    val longitude = _longitude.asStateFlow()

    private val _address = MutableStateFlow("")
    val address = _address.asStateFlow()

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
    fun updateLongitude(longitude: Double) {
        _longitude.value = longitude
    }

    fun updateLatitude(latitude: Double) {
        _latitude.value = latitude
    }

    fun updateAddress(address: String) {
        _address.value = address
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
                    latitude = latitude.value,
                    longitude = longitude.value,
                    address = address.value
                )
            } catch (e: Exception) {
                Log.e("MapViewModel", "savePost: $e")
            }

        }
    }

}
