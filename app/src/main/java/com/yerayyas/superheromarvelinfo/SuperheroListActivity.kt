package com.yerayyas.superheromarvelinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.yerayyas.superheromarvelinfo.data.model.SuperheroDataResponse
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import com.yerayyas.superheromarvelinfo.databinding.ActivitySuperheroListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    }

    private fun setupUI() {
        binding.svSuperhero.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?) = false
        })

        adapter = SuperheroAdapter()

            binding.rvSuperhero.setHasFixedSize(true)
            binding.rvSuperhero.layoutManager = LinearLayoutManager(this@SuperheroListActivity)
            binding.rvSuperhero.adapter = adapter

    }

    private fun searchByName(query: String) {
        Log.d("rrrr", "Search query: $query")
        binding.pbSuperhero.isVisible = true

        val apiKey = "3de6bbd5de0a40038da2c8fe677fb23b"
        val hash = "56feb160b3d944895040bec40ead241b"
        val ts = 1
        // Secondary thread
        CoroutineScope(Dispatchers.IO).launch {
            val myResponse = retrofit
                .create(ApiService::class.java)
                .getSuperheroByName(query, apiKey, hash, ts)

            if (myResponse.isSuccessful){
                Log.d("rrrr", "API call successful")
                Log.i("chochocho", "it works :)")
                val response:SuperheroDataResponse? = myResponse.body()
                if (response != null){
                    Log.d("rrrr", "Response data: ${response.data.superheroes}")
                    Log.i("chochocho", response.toString())
                    runOnUiThread {
                        adapter.updateList(response.data.superheroes)
                        Log.d("rrrr", "recyclerview updated")
                        binding.pbSuperhero.isVisible = false
                    }
                }

            }else{
                Log.d("rrrr", "API call not successful")
                Log.i("chochocho", "it doesn't work :(")
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
}