package com.example.photogalleryapp.View

// PhotoGrid.kt
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.photogalleryapp.model.Photo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoGrid(
    photos: List<Photo>,
    onPhotoClick: (Photo) -> Unit,
    onPhotoLongClick: (Photo) -> Unit  // Ví dụ: xoá hoặc đánh dấu yêu thích
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(1.dp)
    ) {
        items(photos) { photo ->
            Box(
                modifier = Modifier
                    .padding(1.dp)
                    .combinedClickable(
                        onClick = { onPhotoClick(photo) },
                        onLongClick = { onPhotoLongClick(photo) }
                    )
            ) {
                androidx.compose.foundation.Image(
                    painter = rememberAsyncImagePainter(model = photo.uri),
                    contentDescription = "Photo Thumbnail",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }
    }
}
