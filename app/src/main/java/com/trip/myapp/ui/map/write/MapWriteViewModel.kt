package com.trip.myapp.ui.map.write

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.myapp.domain.usecase.AddPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapWriteViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase
) : ViewModel() {
    private val _event = Channel<MapWriteEvent>(64)
    val event = _event.receiveAsFlow()

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

    private val _pinColor = MutableStateFlow(Color.Red)
    val pinColor = _pinColor.asStateFlow()

    private val _latitude = MutableStateFlow(37.50375925598199)
    val latitude = _latitude.asStateFlow()

    private val _longitude = MutableStateFlow(126.95628584359363)
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


    fun resetAll() {
        _title.value = ""
        _content.value = ""
        _selectedImages.value = emptyList()
        _startDate.value = ""
        _endDate.value = ""
        _pinColor.value = Color.Red
        _latitude.value = 37.5665
        _longitude.value = 126.9780
        _address.value = ""
    }

    fun savePost() {
        viewModelScope.launch {
            try {
                val a = (pinColor.value.alpha * 255).toInt() and 0xFF
                val r = (pinColor.value.red * 255).toInt() and 0xFF
                val g = (pinColor.value.green * 255).toInt() and 0xFF
                val b = (pinColor.value.blue * 255).toInt() and 0xFF

                val intColor = android.graphics.Color.argb(a, r, g, b)

                val longColor = intColor.toLong() and 0xFFFFFFFF
                _event.trySend(MapWriteEvent.AddPost.Loading)
                addPostUseCase(
                    title = title.value,
                    content = content.value,
                    selectedImages = selectedImages.value,
                    startDate = startDate.value,
                    endDate = endDate.value,
                    pinColor = longColor,
                    latitude = latitude.value,
                    longitude = longitude.value,
                    address = address.value
                )
                _event.trySend(MapWriteEvent.AddPost.Success)
            } catch (e: Exception) {
                Log.e("MapViewModel", "savePost: $e")
                _event.trySend(MapWriteEvent.AddPost.Failure)
            }

        }
    }
}
