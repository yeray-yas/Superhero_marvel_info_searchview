package com.yerayyas.superheromarvelinfo

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.yerayyas.superheromarvelinfo.DetailSuperheroActivity.Companion.EXTRA_ID
import com.yerayyas.superheromarvelinfo.databinding.ActivitySuperheroListBinding
import com.yerayyas.superheromarvelinfo.viewModel.SuperheroListViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SuperheroListActivity : AppCompatActivity() {

    private val viewModel: SuperheroListViewModel by viewModels()


    private lateinit var binding: ActivitySuperheroListBinding
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: SuperheroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuperheroListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = getRetrofit()
        setupUI()

        // We call the function to load all superheroes at start
        viewModel.loadAllSuperheroes()

        // We observe the changes in the superheroes list
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.superheroesState.collect { superheroes ->
                    adapter.updateList(superheroes)
                }
            }
        }
    }

    private fun setupUI() {
        binding.svSuperhero.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchByName(query.orEmpty())
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
