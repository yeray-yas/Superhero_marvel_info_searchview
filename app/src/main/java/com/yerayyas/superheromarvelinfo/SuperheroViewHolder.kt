package com.yerayyas.superheromarvelinfo

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import com.yerayyas.superheromarvelinfo.databinding.ItemSuperheroBinding

class SuperheroViewHolder(view: View) : ViewHolder(view) {

    private val binding = ItemSuperheroBinding.bind(view)

    fun bind(superheroItemResponse: SuperheroItemResponse, onItemSelected: (Int) -> Unit) {

        // Superhero name
        binding.tvSuperheroName.text = superheroItemResponse.name

        // Superhero image
        val imageUrl =
            "${superheroItemResponse.thumbnail.path}.${superheroItemResponse.thumbnail.extension}"

        if (imageUrl.contains("image_not_available")) {
            Picasso.get().load(R.drawable.marvel_image_not_found).into(binding.ivSuperhero)

        } else {
            Picasso.get().load(imageUrl).into(binding.ivSuperhero)
        }

        // Every single cardview, in each click
        binding.root.setOnClickListener {
            onItemSelected(superheroItemResponse.superheroId)
            Log.i("SHH", "Superhero ID: ${superheroItemResponse.superheroId}")
        }
    }
}
