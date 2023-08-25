package com.yerayyas.superheromarvelinfo.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yerayyas.superheromarvelinfo.R
import com.yerayyas.superheromarvelinfo.ui.list.SuperheroListViewHolder
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse

class SuperheroAdapter(
    private val onItemSelected: (Int) -> Unit
) : PagingDataAdapter<SuperheroItemResponse, SuperheroListViewHolder>(SuperheroDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroListViewHolder {
        return SuperheroListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_superhero, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SuperheroListViewHolder, position: Int) {
        val superheroItem = getItem(position)
        superheroItem?.let {
            holder.bind(it, onItemSelected)
        }
    }
}

class SuperheroDiffCallback : DiffUtil.ItemCallback<SuperheroItemResponse>() {
    override fun areItemsTheSame(oldItem: SuperheroItemResponse, newItem: SuperheroItemResponse): Boolean {
        return oldItem.superheroId == newItem.superheroId
    }

    override fun areContentsTheSame(oldItem: SuperheroItemResponse, newItem: SuperheroItemResponse): Boolean {
        return oldItem == newItem
    }
}
