package com.example.kissabyss.views.SavedImagesGallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.example.kissabyss.R
import com.example.kissabyss.adapters.SavedImagesAdapter
import com.example.kissabyss.databinding.ActivitySavedImagesGalleryBinding
import com.example.kissabyss.listeners.SavedImageListener
import com.example.kissabyss.utilities.displayToast
import com.example.kissabyss.viewmodels.SavedImagesViewModel
import com.example.kissabyss.views.editimage.PickupImageActivity
import com.example.kissabyss.views.filteredImage.FilteredImageActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SavedImagesGalleryActivity : AppCompatActivity(), SavedImageListener {

    private lateinit var binding: ActivitySavedImagesGalleryBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
        setupListenersOnLoad()
        viewModel.loadSavedImages()
    }

    private fun setupListenersOnLoad(){
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupObservers(){
        viewModel.savedImagesUiState.observe(this,{
            val savedImagesDataState = it ?: return@observe
            binding.savedImagesProgressBar.visibility =
                if(savedImagesDataState.isLoading) View.VISIBLE else View.GONE
            savedImagesDataState.savedImages?.let{savedImages ->
                SavedImagesAdapter(savedImages,this).also { savedImagesAdapter ->
                    with(binding.savedImagesRecycler){
                        this.adapter = savedImagesAdapter
                        visibility = View.VISIBLE
                    }
                }
            } ?: kotlin.run {
                savedImagesDataState.error?.let { error ->{
                    displayToast(error)
                } }
            }
        })
    }

    override fun onSavedImageClicked(file: File) {
       val fileUriEx = FileProvider.getUriForFile(
           applicationContext,
           "${packageName}.provider",
           file
       )
        Intent(
            applicationContext,
            FilteredImageActivity::class.java
        ).also { filteredImageIntent ->
            filteredImageIntent.putExtra(PickupImageActivity.KEY_FILTERED_IMAGE_URI,fileUriEx)
            startActivity(filteredImageIntent)
        }
    }

}