package com.example.kissabyss.views.editimage

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.kissabyss.adapters.ImageFiltersAdapter
import com.example.kissabyss.databinding.ActivityPickupImageBinding
import com.example.kissabyss.listeners.ImageFilterPreviewListener
import com.example.kissabyss.processing.ImageFilter
import com.example.kissabyss.utilities.displayToast
import com.example.kissabyss.utilities.show
import com.example.kissabyss.viewmodels.PickupImageViewModel
import com.example.kissabyss.views.filteredImage.FilteredImageActivity
import com.example.kissabyss.views.main.MainActivity
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class PickupImageActivity : AppCompatActivity(), ImageFilterPreviewListener {

    companion object{
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }

    private lateinit var binding : ActivityPickupImageBinding
    private val viewModel:PickupImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    //bitmaps
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickupImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListenersOnLoad()
        setupObserversOnLoad()
        prepareImagePreview()
    }

    private fun prepareImagePreview(){
        gpuImage = GPUImage(applicationContext)
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
                //Normal filter applied by default
                originalBitmap = bitmap
                filteredBitmap.value = bitmap
                with(originalBitmap){
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(bitmap)
                }

            } ?: kotlin.run {
                dataState.error?.let { error ->
                    this.displayToast(error)
                }
            }
        })

        filteredBitmap.observe(this, {
            binding.imagePreview.setImageBitmap(it)

        })

        viewModel.imageFiltersUiState.observe(this,{
            val imageFilterDataState = it ?: return@observe
            binding.onFiltersLoadProgressBar.visibility =
                if(imageFilterDataState.isLoading) View.VISIBLE else View.GONE
            imageFilterDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters,this).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
            } ?: run {
                imageFilterDataState.error?.let { error -> this.displayToast(error) }
            }
        })

        viewModel.saveFilteredImageUiState.observe(this,{
            val saveFilteredImageDataState = it ?: return@observe
            if(saveFilteredImageDataState.isLoading){
                binding.imageSave.visibility = View.GONE
                binding.onFileSavingProgressBar.visibility = View.VISIBLE
            } else {
                binding.onFileSavingProgressBar.visibility = View.GONE
                binding.imageSave.visibility = View.VISIBLE
            }
            saveFilteredImageDataState.uri?.let { savedImageUri ->
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also { savedImageIntent ->
                    savedImageIntent.putExtra(KEY_FILTERED_IMAGE_URI,savedImageUri)
                    startActivity(savedImageIntent)
                }
            } ?: kotlin.run {
                saveFilteredImageDataState.error?.let {error->
                    this.displayToast(error.toString())
                }
            }
        })
    }


    private fun setListenersOnLoad(){
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }

        /*Original image on long hover*/
        binding.imagePreview.setOnLongClickListener {
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        /*And on release */
        binding.imagePreview.setOnClickListener {
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }

        binding.imageSave.setOnClickListener {
            filteredBitmap.value?.let { bitmap ->
                viewModel.saveFilteredImage(bitmap)
            }
        }

    }

    override fun onFilterClicked(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}