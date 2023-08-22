package com.yerayyas.superheromarvelinfo

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.squareup.picasso.Picasso
import com.yerayyas.superheromarvelinfo.data.imageModel.ImageDatasResult
import com.yerayyas.superheromarvelinfo.databinding.ActivityDetailSuperheroBinding
import com.yerayyas.superheromarvelinfo.viewModel.DetailSuperheroViewModel
import kotlinx.coroutines.launch

class DetailSuperheroActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityDetailSuperheroBinding
    private val viewModel: DetailSuperheroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSuperheroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id: Int = intent.getIntExtra(EXTRA_ID, -1)

        // We observe the changes in the superheroes list
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.superheroInfo.collect { superhero ->
                    superhero?.let {
                        createUI(it)
                    }
                }
            }
        }

        viewModel.getSuperheroInfo(id)
    }

    private fun createUI(superhero: ImageDatasResult) {
        val thumbnail = superhero.data.results[0].thumbnail

        Log.d("DSA", "Thumbnail Path: ${thumbnail.path}")
        Log.d("DSA", "Thumbnail Extension: ${thumbnail.extension}")
        val imageUrl = "${thumbnail.path}.${thumbnail.extension}"
        if (imageUrl.contains("image_not_available")) {
            Picasso.get().load(R.drawable.marvel_image_not_found).into(binding.ivSuperhero)

        } else {
            Picasso.get().load(imageUrl).into(binding.ivSuperhero)
        }

        binding.tvSuperheroName.text = superhero.data.results[0].name
        binding.tvSuperheroDescription.text = superhero.data.results[0].description
    }
}
