package com.example.kissabyss.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kissabyss.R
import com.example.kissabyss.databinding.ItemFilterContainerBinding
import com.example.kissabyss.listeners.ImageFilterPreviewListener
import com.example.kissabyss.processing.ImageFilter

class ImageFiltersAdapter(
    private val imageFilters: List<ImageFilter>,
    private val imageFilterListener: ImageFilterPreviewListener
    ) : RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>() {

    private var selectedItemPosition = 0
    private var previouslySelectedItemPosition = 0

    inner class ImageFilterViewHolder(val binding: ItemFilterContainerBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding = ItemFilterContainerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(imageFilters[position]){
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name
                binding.root.setOnClickListener {
                    if(position!=selectedItemPosition){
                        imageFilterListener.onFilterClicked(this)
                        previouslySelectedItemPosition = selectedItemPosition
                        selectedItemPosition = position
                        with(this@ImageFiltersAdapter){
                            notifyItemChanged(previouslySelectedItemPosition,Unit)
                            notifyItemChanged(selectedItemPosition,Unit)
                        }
                    }
                }
            }
            binding.textFilterName.setTextColor(
                ContextCompat.getColor(binding.textFilterName.context,
                if(selectedItemPosition == position) R.color.primaryDark
                else R.color.primaryText
            ))
        }
    }

    override fun getItemCount(): Int = imageFilters.size
}