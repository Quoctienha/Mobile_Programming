package com.example.photogalleryapp.View

// FullPhotoView.kt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.example.photogalleryapp.model.Photo

@Composable
fun FullPhotoView(
    photos: List<Photo>,
    currentIndex: Int,
    onNavigate: (newIndex: Int) -> Unit,
    onBack: () -> Unit
) {
    // State để xử lý scale và offset khi phóng to/thu nhỏ
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)


    ) {

        // Hiển thị ảnh toàn màn hình với hiệu ứng scale và offset
        Image(
            painter = rememberAsyncImagePainter(model = photos[currentIndex].uri),
            contentDescription = "Full Photo",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                },
            contentScale = ContentScale.Fit

        )

        // Nút điều hướng "Trước"
        IconButton(
            onClick = { if (currentIndex > 0) onNavigate(currentIndex - 1) },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        // Nút điều hướng "Sau"
        IconButton(
            onClick = { if (currentIndex < photos.size - 1) onNavigate(currentIndex + 1) },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Back", tint = Color.White)

        }

        // Nút Close để quay lại grid view
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
        }
    }
}

