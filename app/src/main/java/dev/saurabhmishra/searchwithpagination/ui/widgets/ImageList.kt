package dev.saurabhmishra.searchwithpagination.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import dev.saurabhmishra.searchwithpagination.mappers.getPhotoUrl
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.ui.theme.SearchWithPaginationTheme
import kotlinx.coroutines.flow.flow


@Composable
fun ImageList(
    photosPagingItems: LazyPagingItems<PhotoEntity>,
    onLikeIconClick: (PhotoEntity) -> Unit
) {

    val loadState = photosPagingItems.loadState
    if (loadState.refresh is LoadState.Loading) {
        ScreenWideProgress()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(photosPagingItems) { photo ->
                photo?.let {
                    PhotoWithLikeButton(photo, onLikeIconClick)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            if (loadState.append is LoadState.Loading) {
                item {
                    ProgressItem()
                }
            }
        }
    }
}

@Composable
private fun ScreenWideProgress() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ProgressItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PhotoWithLikeButton(photo: PhotoEntity, onLikeIconClick: (PhotoEntity) -> Unit) {
    // A height is compulsory to be provided, without this, rememberImagePainter
    // does not work.
    // For reference -> https://coil-kt.github.io/coil/compose/
    Box {
        val roundedCorner = with(LocalDensity.current) { 4.dp.toPx() }
        Image(
            painter = rememberImagePainter(
                data = photo.getPhotoUrl(),
                builder = {
                    transformations(RoundedCornersTransformation(roundedCorner))
                }
            ),
            contentDescription = photo.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        IconButton(
            onClick = { onLikeIconClick.invoke(photo) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp)
        ) {
            Icon(getLikeIcon(photo), contentDescription = "Like button")
        }
    }
}

private fun getLikeIcon(photo: PhotoEntity): ImageVector {
    return if (photo.isFavorite) {
        Icons.Filled.Favorite
    } else {
        Icons.Filled.ThumbUp
    }
}

@Preview
@Composable
fun ImageListPreview() {
    val flowing = flow<PagingData<PhotoEntity>> {}
    val photoPagingItems = flowing.collectAsLazyPagingItems()

    SearchWithPaginationTheme {
        ImageList(photosPagingItems = photoPagingItems, onLikeIconClick = {})
    }
}