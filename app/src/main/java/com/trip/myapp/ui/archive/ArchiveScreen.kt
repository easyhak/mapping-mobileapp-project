package com.trip.myapp.ui.archive

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trip.myapp.R

@Composable
fun ArchiveScreen(onDetailClick: (String) -> Unit) {

    // todo data class로 변경
    val sampleCategoryItems = listOf(
        Pair("여행1", R.drawable.category_sample_1),
        Pair("여행2", R.drawable.category_sample_2),
        Pair("여행3", R.drawable.category_sample_3),
    )


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // 아이템 간의 가로 간격
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sampleCategoryItems.size) { index ->
            CardItem(
                title = sampleCategoryItems[index].first,
                imageRes = sampleCategoryItems[index].second,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun CardItem(title: String, imageRes: Int, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}




@Preview(showBackground = true)
@Composable
private fun PreviewArchiveScreen() {
    ArchiveScreen(
        onDetailClick = {}
    )
}
