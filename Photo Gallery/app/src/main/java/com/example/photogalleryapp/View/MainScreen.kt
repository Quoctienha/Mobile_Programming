package com.example.photogalleryapp.View

// MainScreen.kt
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.photogalleryapp.ViewModel.PhotoViewModel
import com.example.photogalleryapp.model.Photo
import java.io.File




@Composable
fun MainScreen(
    viewModel: PhotoViewModel = viewModel()
) {

    // State để theo dõi màn hình hiện tại: Grid view hoặc Full photo view
    var isFullScreen by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    // Launcher mở camera, để thêm ảnh
    val context = LocalContext.current
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        val uri = cameraUri
        if (success && uri != null) {
            viewModel.addPhoto(uri.toString())
        }
    }
    // Dialog xác nhận xóa ảnh
    var showDeleteDialog by remember { mutableStateOf(false) }
    var photoToDelete by remember { mutableStateOf<Photo?>(null) }

    if (showDeleteDialog && photoToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xác nhận") },
            text = { Text("Bạn có chắc chắn muốn xóa ảnh này không?") },
            confirmButton = {
                TextButton(onClick = {
                    photoToDelete?.let {
                        viewModel.removePhoto(it) // Xoá ảnh nếu người dùng xác nhận
                    }
                    showDeleteDialog = false
                }) {
                    Text("Xóa", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                }) {
                    Text("Hủy")
                }
            }
        )
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val photoFile = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "IMG_${System.currentTimeMillis()}.jpg"
                )
                cameraUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )
                cameraUri?.let { cameraLauncher.launch(it) }
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Photo",
                    tint = Color.Black
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Animation chuyển đổi giữa grid và full view
            // Dùng để tạo hiệu ứng chuyển đổi mượt mà giữa hai chế độ hiển thị
            AnimatedVisibility(visible = !isFullScreen) {
                PhotoGrid(
                    photos = viewModel.photos,
                    onPhotoClick = { photo ->
                        selectedIndex = viewModel.photos.indexOf(photo)
                        isFullScreen = true
                    },
                    onPhotoLongClick = { photo ->
                        // Hiển thị dialog xác nhận khi long press
                        photoToDelete = photo
                        showDeleteDialog = true
                    }
                )
            }
            AnimatedVisibility(visible = isFullScreen) {
                FullPhotoView(
                    photos = viewModel.photos,
                    currentIndex = selectedIndex,
                    onNavigate = { newIndex ->
                        selectedIndex = newIndex
                    },
                    onBack = { isFullScreen = false }
                )
            }
        }
    }
}
