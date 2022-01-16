package com.example.kissabyss.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.example.kissabyss.processing.ImageFilter

interface EditImageRepository {
    suspend fun decodeImageFromUri(imageUri: Uri): Bitmap?
    suspend fun getImageFilters(image: Bitmap) : List<ImageFilter>
    suspend fun saveFilteredImage(filteredBitmap: Bitmap) : Uri?
}