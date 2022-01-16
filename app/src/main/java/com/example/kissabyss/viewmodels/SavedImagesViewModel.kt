package com.example.kissabyss.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kissabyss.repositories.SavedImagesRepository
import com.example.kissabyss.utilities.Coroutines
import java.io.File

class SavedImagesViewModel(private val savedImagesRepository: SavedImagesRepository) : ViewModel() {

    private val savedImagesDataState = MutableLiveData<SavedImagesDataState>()
    val savedImagesUiState: LiveData<SavedImagesDataState> get() = savedImagesDataState

    fun loadSavedImages(){
        Coroutines.runJobInIO {
            kotlin.runCatching {
                emitSavedImagesUiState(isLoading = true)
                savedImagesRepository.loadSavedImages()
            }.onSuccess { savedImages ->
                if(savedImages.isNullOrEmpty()){
                    emitSavedImagesUiState(error = "no images found")
                } else {
                    emitSavedImagesUiState(savedImages = savedImages)
                }
            }.onFailure {
                emitSavedImagesUiState(error = it.message.toString())
            }

        }
    }

    private fun emitSavedImagesUiState(
        isLoading: Boolean = false,
        savedImages: List<Pair<File, Bitmap>>? = null,
        error: String? = null
    ){
        val dataState = SavedImagesDataState(isLoading,savedImages,error)
        savedImagesDataState.postValue(dataState)
    }


    data class SavedImagesDataState(
        val isLoading : Boolean,
        val savedImages: List<Pair<File,Bitmap>>?,
        val error: String?
    )
}