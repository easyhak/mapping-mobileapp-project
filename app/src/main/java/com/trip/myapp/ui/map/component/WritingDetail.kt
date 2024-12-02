package com.trip.myapp.ui.map.component

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trip.myapp.ui.theme.AppTheme


// ModalBottomSheet 안의 내용을 별도로 분리한 컴포저블 함수
@Composable
fun BottomSheetAddContent(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    selectedImages: List<String>,
    onImagesChange: (List<String>) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // bottom sheet 제목
        ShowingTitle()

        // 제목
        WritingTitle(
            title = title,
            onTitleChange = onTitleChange
        )

        //내용
        WritingExplanation(
            content = content,
            onContentChange = onContentChange
        )
        //사진
        PickingPhoto(
            selectedImages = selectedImages,
            onImagesChange = onImagesChange
        )

        //날짜
        PickingDate()

        //지도
        PickingLocation()
    }
}

@Composable
private fun ShowingTitle() {
    Text(
        text = "여행 기록하기",
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable

private fun WritingTitle(
    title: String,
    onTitleChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        placeholder = {
            Text("제목을 입력하세요")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(60.dp),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        )
    )
}

@Composable
private fun WritingExplanation(
    content: String,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = content,
        onValueChange = onContentChange,
        placeholder = {
            Text("내용을 입력하세요")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .height(250.dp),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults
            .colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            )
    )
}

@Composable
private fun PickingPhoto(
    selectedImages: List<String>,
    onImagesChange: (List<String>) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris ->
            val names = uris.mapNotNull { uri ->
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    if (nameIndex != -1) cursor.getString(nameIndex) else null
                }
            }
            // 선택된 이미지 목록 업데이트
            onImagesChange(names)
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 아이콘 클릭 시 PhotoPicker 실행
        Icon(
            imageVector = Icons.Outlined.Image,
            contentDescription = "image",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
                .clickable {
                    launcher.launch(arrayOf("image/*"))
                }
        )

        // 선택한 이미지 이름 표시
        Column(modifier = Modifier.padding(start = 8.dp)) {
            if (selectedImages.isNotEmpty()) {
                selectedImages.forEach { imageName ->
                    Text(
                        text = imageName,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            } else {
                Text(
                    text = "이미지를 선택하세요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun PickingDate() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.CalendarMonth,
            contentDescription = "calendar",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)


        )
    }
}

@Composable
private fun PickingLocation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.PushPin,
            contentDescription = "pin",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewBottomSheet() {
    AppTheme {
        BottomSheetAddContent(
            title = "여행 기록",
            content = "우와",
            onTitleChange = {},
            onContentChange = {},
            selectedImages = emptyList(),
            onImagesChange = {},

        )
    }
}

