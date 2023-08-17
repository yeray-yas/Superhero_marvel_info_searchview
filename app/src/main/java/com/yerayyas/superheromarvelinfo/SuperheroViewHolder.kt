package com.yerayyas.superheromarvelinfo

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import com.yerayyas.superheromarvelinfo.databinding.ItemSuperheroBinding

class SuperheroViewHolder(view: View) : ViewHolder(view) {

    private val binding = ItemSuperheroBinding.bind(view)

    fun bind(superheroItemResponse: SuperheroItemResponse) {

        binding.tvSuperheroName.text = superheroItemResponse.name



    }

}
