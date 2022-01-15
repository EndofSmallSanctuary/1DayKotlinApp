package com.example.kissabyss

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import com.example.kissabyss.databinding.ActivityMainBinding
import com.example.kissabyss.databinding.ActivityPickupImageBinding

class PickupImageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPickupImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickupImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListenersOnLoad()
        displayImagePreview()
    }

    private fun displayImagePreview(){
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.imagePreview.setImageBitmap(bitmap)
        }
    }

    private fun setListenersOnLoad(){
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }
    }
}