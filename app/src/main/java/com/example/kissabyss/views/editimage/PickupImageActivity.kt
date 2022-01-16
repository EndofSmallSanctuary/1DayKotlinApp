package com.example.kissabyss.views.editimage

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
import com.example.kissabyss.views.main.MainActivity
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class PickupImageActivity : AppCompatActivity(), ImageFilterPreviewListener {

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