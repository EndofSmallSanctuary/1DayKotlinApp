package com.example.kissabyss.repositories.impls

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.kissabyss.repositories.EditImageRepository
import java.io.InputStream

class EditImageRepositoryImpl(private val context: Context) : EditImageRepository  {
    override suspend fun decodeImageFromUri(imageUri: Uri): Bitmap? {
        getInputStreamFromUri(imageUri)?.let { inputStream ->
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val width = context.resources.displayMetrics.widthPixels
            val height = ((originalBitmap.height*width) / originalBitmap.width)
            return Bitmap.createScaledBitmap(originalBitmap,width,height,false)
        } ?: return null
    }

    private fun getInputStreamFromUri(uri: Uri): InputStream?{
        return context.contentResolver.openInputStream(uri)
    }
}