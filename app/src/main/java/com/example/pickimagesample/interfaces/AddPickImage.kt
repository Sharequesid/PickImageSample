package com.example.pickimagesample.interfaces

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun AddPickImage(maxSelectionCount: Int = 1) {
    Surface {
        val notesList = remember {
            mutableStateListOf<Uri?>()
        }


        val buttonText = if (maxSelectionCount > 1) {
            "Select up to $maxSelectionCount photos"
        } else {
            "Select a photo"
        }

        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> notesList.add(uri) }
        )

        // I will start this off by saying that I am still learning Android development:
        // We are tricking the multiple photos picker here which is probably not the best way,
        // if you know of a better way to implement this feature drop a comment and let me know
        // how to improve this design
        val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = if (maxSelectionCount > 1) {
                maxSelectionCount
            } else {
                2
            }),
            onResult = {
                    uris ->
                notesList.clear()
                notesList.addAll(uris)
            }
        )

        fun launchPhotoPicker() {
            if (maxSelectionCount > 1) {
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            } else {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                launchPhotoPicker()
            }) {
                Text(buttonText)
            }

            ImageLayoutView(selectedImages = notesList,
                OnRemove = {data-> notesList.remove(data)})
        }
    }
}

@Composable
fun ImageLayoutView(selectedImages: List<Uri?>,OnRemove:(Uri?)->Unit) {
    LazyColumn {
        items(selectedImages) { uri ->
            Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .width(200.dp)
                        .padding(top = 10.dp),
                    contentScale = ContentScale.Fit
                )

                IconButton(onClick = { OnRemove(uri) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete ,
                        contentDescription = "",
                        tint = Color.Red,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }

        }
    }
}