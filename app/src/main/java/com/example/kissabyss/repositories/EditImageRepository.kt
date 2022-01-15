package com.example.kissabyss.repositories

import android.graphics.Bitmap
import android.net.Uri

interface EditImageRepository {
    suspend fun decodeImageFromUri(imageUri: Uri): Bitmap?
}