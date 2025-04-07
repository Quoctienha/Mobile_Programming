package com.example.photogalleryapp.ViewModel

// PhotoViewModel.kt
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.photogalleryapp.model.Photo

class PhotoViewModel : ViewModel() {
    // Sử dụng mutableStateListOf để Compose tự động recompose khi danh sách thay đổi
    private val _photos = mutableStateListOf<Photo>()
    val photos: List<Photo> get() = _photos

    // Giả sử id tăng dần
    private var nextId = _photos.size

    fun addPhoto(uri: String) {
        _photos.add(Photo(id = nextId++, uri = uri))
    }

    fun removePhoto(photo: Photo) {
        _photos.remove(photo)
    }
}