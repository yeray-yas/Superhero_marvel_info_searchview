package com.yerayyas.superheromarvelinfo.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yerayyas.superheromarvelinfo.R
import com.yerayyas.superheromarvelinfo.ui.list.SuperheroListViewHolder
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse

class SuperheroAdapter(
    private var superheroList: List<SuperheroItemResponse> = emptyList(),
    private val onItemSelected: (Int) -> Unit
) :
    RecyclerView.Adapter<SuperheroListViewHolder>() {

    // Método para actualizar la lista de superhéroes y notificar cambios
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(superheroList: List<SuperheroItemResponse>) {
        this.superheroList = superheroList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroListViewHolder {
        return SuperheroListViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_superhero, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SuperheroListViewHolder, position: Int) {
        val superheroItem = superheroList[position]
        Log.d("fuj", "Binding superhero at position $position: ${superheroItem.name}")
        holder.bind(superheroItem, onItemSelected)
    }

    override fun getItemCount() = superheroList.size
}