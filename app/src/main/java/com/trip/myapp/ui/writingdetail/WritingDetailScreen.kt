package com.trip.myapp.ui.writingdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet() {
    // BottomSheet 상태를 관리하는 변수
    var showBottomSheet by remember { mutableStateOf(false) }

    // ModalBottomSheetState는 하단 시트의 상태를 관리
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false // 부분적으로 확장되는 동작을 허용
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 하단 시트를 표시할 버튼(원래에는 floating button)
        Button(
            onClick = { showBottomSheet = true }
        ) {
            Text("Display partial bottom sheet")
        }

        // BottomSheet가 표시될 때
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(), // 시트의 높이를 화면의 100%로 채움
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false } // 시트를 닫을 때
            ) {
                // BottomSheet 내용은 이제 별도의 함수로 분리됨
                BottomSheetContent()
            }
        }
    }
}

// ModalBottomSheet 안의 내용을 별도로 분리한 컴포저블 함수
@Composable
fun BottomSheetContent() {
    // TextField 값 상태
    var textValue by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // 전체적인 padding을 설정
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 중앙에 배치된 "여행 기록하기" 텍스트
        Text(
            text = "여행 기록하기",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 제목을 입력하는 TextField
        OutlinedTextField(
            value = textValue,
            onValueChange = { textValue = it },
            label = { Text("제목을 입력하세요") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .height(40.dp),
            shape = MaterialTheme.shapes.medium // 모서리를 둥글게 설정
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPartialBottomSheet() {
    PartialBottomSheet()
}
