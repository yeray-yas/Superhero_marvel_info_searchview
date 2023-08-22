package com.yerayyas.superheromarvelinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.yerayyas.superheromarvelinfo.DetailSuperheroActivity.Companion.EXTRA_ID
import com.yerayyas.superheromarvelinfo.data.model.SuperheroDataResponse
import com.yerayyas.superheromarvelinfo.databinding.ActivitySuperheroListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SuperheroListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuperheroListBinding
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: SuperheroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuperheroListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = getRetrofit()
        setupUI()

        // We call the function to load all superheroes
        loadAllSuperheroes()
    }

    private fun setupUI() {
        binding.svSuperhero.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?) = false
        })

        adapter = SuperheroAdapter { superheroId -> // "Generic it" have been replaced by "superheroId"
            navigateToDetail(superheroId)
        }
        binding.rvSuperhero.setHasFixedSize(true)
        binding.rvSuperhero.layoutManager = LinearLayoutManager(this@SuperheroListActivity)
        binding.rvSuperhero.adapter = adapter
    }

    private fun loadAllSuperheroes() {
        binding.pbSuperhero.isVisible = true

        val apiKey = "3de6bbd5de0a40038da2c8fe677fb23b"
        val hash = "56feb160b3d944895040bec40ead241b"
        val ts = 1

        CoroutineScope(Dispatchers.IO).launch {
            val myResponse = retrofit
                .create(ApiService::class.java)
                .getSuperheroes(apiKey, hash, ts)

            if (myResponse.isSuccessful) {
                val response: SuperheroDataResponse? = myResponse.body()
                if (response != null) {
                    runOnUiThread {
                        adapter.updateList(response.data.superheroes)
                        binding.pbSuperhero.isVisible = false
                    }
                }
            } else {
                Log.d("rrrr", "API call not successful")
            }
        }
    }

    private fun searchByName(query: String) {
        binding.pbSuperhero.isVisible = true

        val apiKey = "3de6bbd5de0a40038da2c8fe677fb23b"
        val hash = "56feb160b3d944895040bec40ead241b"
        val ts = 1

        CoroutineScope(Dispatchers.IO).launch {
            val response: SuperheroDataResponse? = if (query.isBlank()) {
                runBlocking {
                    retrofit.create(ApiService::class.java).getSuperheroes(apiKey, hash, ts).body()
                }
            } else {
                runBlocking {
                    retrofit.create(ApiService::class.java)
                        .getSuperheroByName(query, apiKey, hash, ts).body()
                }
            }

            runOnUiThread {
                if (response != null) {
                    adapter.updateList(response.data.superheroes)
                }
                binding.pbSuperhero.isVisible = false
            }
        }
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun navigateToDetail(id: Int) {
        val intent = Intent(this, DetailSuperheroActivity::class.java)
        intent.putExtra(EXTRA_ID, id)
        startActivity(intent)
    }
}
