package com.yerayyas.superheromarvelinfo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.yerayyas.superheromarvelinfo.data.imageModel.ImageDatasResult
import com.yerayyas.superheromarvelinfo.databinding.ActivityDetailSuperheroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailSuperheroActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityDetailSuperheroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSuperheroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id: Int = intent.getIntExtra(EXTRA_ID, -1)

        try {
            getSuperheroInfo(id)
        } catch (e: Exception) {
            Log.e("DSA", "Error loading superhero info: ${e.message}")
            // Handle the exception, show an error message, etc.
        }
    }

    private fun getSuperheroInfo(id: Int) {
        val apiKey = "3de6bbd5de0a40038da2c8fe677fb23b"
        val hash = "56feb160b3d944895040bec40ead241b"
        val ts = 1

        val url =
            "https://gateway.marvel.com/v1/public/characters/$id?apikey=$apiKey&hash=$hash&ts=$ts"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val superheroDetail = getRetrofit()
                    .create(ApiService::class.java)
                    .getSuperheroesDetail(id, apiKey, hash, ts)

                val response = superheroDetail.body()
                if (response != null) {
                    Log.d("DSA", "API Response: $response")
                    runOnUiThread {
                        try {
                            createUI(response, url)
                        } catch (e: Exception) {
                            Log.e("DSA", "Error creating UI: ${e.message}")
                            // Handle the exception, show an error message, etc.
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("DSA", "API call error: ${e.message}")
                // Handle the exception, show an error message, etc.
            }
        }
    }

    private fun createUI(superhero: ImageDatasResult, url: String) {
        Log.d("DSA", "API URL: $url")
        val thumbnail = superhero.data.results[0].thumbnail

        if (thumbnail != null) {
            Log.d("DSA", "Thumbnail Path: ${thumbnail.path}")
            Log.d("DSA", "Thumbnail Extension: ${thumbnail.extension}")
            val imageUrl = "${thumbnail.path}.${thumbnail.extension}"
            if (imageUrl.contains("image_not_available")) {
                Picasso.get().load(R.drawable.marvel_image_not_found).into(binding.ivSuperhero)

            } else {
                Picasso.get().load(imageUrl).into(binding.ivSuperhero)
            }
        } else {
            Log.e("DSA", "Thumbnail is null")
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
            // Manejar el caso en que no hay imagen disponible
            // Puedes mostrar una imagen de marcador de posición o un mensaje de error
            Picasso.get().load(R.drawable.marvel_image_not_found).into(binding.ivSuperhero)

        }

        binding.tvSuperheroName.text = superhero.data.results[0].name
        binding.tvSuperheroDescription.text = superhero.data.results[0].description
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
