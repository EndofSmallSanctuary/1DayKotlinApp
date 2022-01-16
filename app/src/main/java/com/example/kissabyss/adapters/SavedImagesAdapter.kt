package com.example.kissabyss.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kissabyss.databinding.ItemContainerSavedImageBinding
import com.example.kissabyss.listeners.SavedImageListener
import java.io.File

class SavedImagesAdapter(private val savedImages: List<Pair<File,Bitmap>>, private val savedImageListener: SavedImageListener) :
    RecyclerView.Adapter<SavedImagesAdapter.SavedImagesViewHolder>()  {

    inner class SavedImagesViewHolder(val binding: ItemContainerSavedImageBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImagesViewHolder {
        val binding = ItemContainerSavedImageBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return SavedImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImagesViewHolder, position: Int) {
        with(holder){
            with(savedImages[position]){
                binding.imageSaved.setImageBitmap(second)
                binding.imageSaved.setOnClickListener{
                    savedImageListener.onSavedImageClicked(first)
                }
            }
        }
    }


    override fun getItemCount(): Int = savedImages.size
}