package com.example.kissabyss.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kissabyss.processing.ImageFilter
import com.example.kissabyss.repositories.EditImageRepository
import com.example.kissabyss.utilities.Coroutines

class PickupImageViewModel(private val editImageRepository: EditImageRepository) : ViewModel() {

    //region :: Prepare Image Preview


    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri){
        Coroutines.runJobInIO {
            kotlin.runCatching {
                emitImagePreviewUIState(isLoading = true)
                editImageRepository.decodeImageFromUri(imageUri)
            }.onSuccess { bitmap ->
                if(bitmap!=null)
                emitImagePreviewUIState(bitmap = bitmap) else { emitImagePreviewUIState(error = "Unable during image parsing") }
            }.onFailure { errorMessage->
                emitImagePreviewUIState(error = errorMessage.message.toString())
            }
        }
    }

    private fun emitImagePreviewUIState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ) {
        val dataState = ImagePreviewDataState(isLoading,bitmap,error)
        imagePreviewDataState.postValue(dataState)
    }

    data class ImagePreviewDataState(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?
    )

    //endregion

    //region:: Prepare image Filters
    private val imageFiltersDataState =  MutableLiveData<ImageFiltersDataState>()
    

    data class ImageFiltersDataState(val isLoading: Boolean,val imageFilters: List<ImageFilter>, val error: String?)

    //endregion
}