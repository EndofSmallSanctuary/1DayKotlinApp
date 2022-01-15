package com.example.kissabyss.views.editimage

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.kissabyss.databinding.ActivityPickupImageBinding
import com.example.kissabyss.utilities.displayToast
import com.example.kissabyss.utilities.show
import com.example.kissabyss.viewmodels.PickupImageViewModel
import com.example.kissabyss.views.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PickupImageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPickupImageBinding

    private val viewModel:PickupImageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickupImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListenersOnLoad()
        setupObserversOnLoad()
        prepareImagePreview()
    }

    private fun prepareImagePreview(){
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }

    private fun setupObserversOnLoad(){
        viewModel.imagePreviewUiState.observe(this,{
            val dataState = it ?: return@observe
            binding.onImageLoadProgressBar.visibility =
                if(dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                binding.imagePreview.setImageBitmap(bitmap)
                binding.imagePreview.show()
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    this.displayToast(error)
                }
            }
        })
    }


    private fun setListenersOnLoad(){
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }
    }
}