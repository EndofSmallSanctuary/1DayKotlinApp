package com.example.kissabyss.listeners

import com.example.kissabyss.processing.ImageFilter

interface ImageFilterPreviewListener {
    fun onFilterClicked(imageFilter: ImageFilter)
}