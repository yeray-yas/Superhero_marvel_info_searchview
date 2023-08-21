package com.yerayyas.superheromarvelinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.yerayyas.superheromarvelinfo.data.model.SuperheroResultsResponse
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

        //getSuperheroInfo(id)
    }

    private fun getSuperheroInfo(id: Int) {
        val apiKey = "3de6bbd5de0a40038da2c8fe677fb23b"
        val hash = "56feb160b3d944895040bec40ead241b"
        val ts = 1
        CoroutineScope(Dispatchers.IO).launch {
            val superheroDetail = getRetrofit()
                .create(ApiService::class.java)
                .getSuperheroesDetail(id, apiKey, hash, ts)
            val response = superheroDetail.body()
            if (response != null) {
                runOnUiThread {
                    createUI(response)
                }

            }
        }
    }

    private fun createUI(superhero: SuperheroResultsResponse) {
        // Superhero image
        val imageUrl =
            "${superhero.thumbnail.path}.${superhero.thumbnail.extension}"
        Picasso.get().load(imageUrl).into(binding.ivSuperhero)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}