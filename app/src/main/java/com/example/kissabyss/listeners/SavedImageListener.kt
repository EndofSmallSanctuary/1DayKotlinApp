package com.example.kissabyss.listeners

import java.io.File

interface SavedImageListener {
    fun onSavedImageClicked(file: File)
}