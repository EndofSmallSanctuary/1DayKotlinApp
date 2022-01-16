package com.example.kissabyss.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kissabyss.databinding.ItemFilterContainerBinding
import com.example.kissabyss.processing.ImageFilter

class ImageFiltersAdapter(private val imageFilters: List<ImageFilter>) : RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>() {

    inner class ImageFilterViewHolder(val binding: ItemFilterContainerBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding = ItemFilterContainerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
        with(holder){
            with(imageFilters[position]){
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name
            }
        }
    }

    override fun getItemCount(): Int = imageFilters.size
}