package com.yerayyas.superheromarvelinfo.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.yerayyas.superheromarvelinfo.data.repository.SuperheroRepository
import com.yerayyas.superheromarvelinfo.ui.detail.DetailSuperheroActivity.Companion.EXTRA_ID
import com.yerayyas.superheromarvelinfo.databinding.ActivitySuperheroListBinding
import com.yerayyas.superheromarvelinfo.network.ApiManager
import com.yerayyas.superheromarvelinfo.network.ApiService
import com.yerayyas.superheromarvelinfo.ui.adapters.SuperheroAdapter
import com.yerayyas.superheromarvelinfo.ui.detail.DetailSuperheroActivity
import com.yerayyas.superheromarvelinfo.util.SuperheroListViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit


class SuperheroListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuperheroListBinding
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: SuperheroAdapter
    private lateinit var repository: SuperheroRepository

    private val viewModelFactory: SuperheroListViewModelFactory by lazy {
        SuperheroListViewModelFactory(repository)
    }

    private val viewModel: SuperheroListViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuperheroListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el repository aquí
        val apiService = ApiManager.retrofit.create(ApiService::class.java)
        repository = SuperheroRepository(apiService)

        retrofit = ApiManager.retrofit
        setupUI()

        // We observe the load state
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingState.collect { state ->
                    when (state) {
                        SuperheroListState.LOADING -> binding.pbSuperhero.isVisible = true
                        else -> binding.pbSuperhero.isVisible = false
                    }
                }
            }
        }

        // We observe the changes in the superheroes list
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.superheroesState.collect { superheroes ->
                    adapter.updateList(superheroes)
                }
            }
        }

        // We call the function to load all superheroes at start
        viewModel.loadAllSuperheroes()
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

    private fun navigateToDetail(id: Int) {
        val intent = Intent(this, DetailSuperheroActivity::class.java)
        intent.putExtra(EXTRA_ID, id)
        startActivity(intent)
    }
}
