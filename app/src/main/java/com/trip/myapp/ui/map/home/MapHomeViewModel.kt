package com.trip.myapp.ui.map.home


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.myapp.domain.model.Post
import com.trip.myapp.domain.usecase.FetchPostByUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapHomeViewModel @Inject constructor(
    private val fetchPostByUserIdUseCase: FetchPostByUserIdUseCase,
) : ViewModel() {

    private val _postList = MutableStateFlow<List<Post>>(emptyList())
    val postList = _postList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = Channel<String>()
    val error = _error.receiveAsFlow()

    init {
        fetchPostList()
    }

    fun fetchPostList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _postList.value = fetchPostByUserIdUseCase()
            } catch (e: Exception) {
                Log.e("MapHomeViewModel", "fetchPostList: $e")
                _error.send("Failed to fetch post list")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
