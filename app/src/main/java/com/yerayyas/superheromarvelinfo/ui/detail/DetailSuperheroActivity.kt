package com.yerayyas.superheromarvelinfo.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import com.yerayyas.superheromarvelinfo.R
import com.yerayyas.superheromarvelinfo.data.model.detailModel.ImageDatasResult
import com.yerayyas.superheromarvelinfo.databinding.ActivityDetailSuperheroBinding
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

        // Observe the error state
        lifecycleScope.launch {
            viewModel.errorState.collect { hasError ->
                if (hasError) {
                    // Handle the error, show an error message, etc.
                    // For example, you can display an error message in a TextView.
                   Log.i("DSA", "An error occurred while loading superhero details")
                }
            }
        }

        // Observe the superhero details
        lifecycleScope.launch {
            viewModel.superheroInfo.collect { superhero ->
                superhero?.let {
                    createUI(it)
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

