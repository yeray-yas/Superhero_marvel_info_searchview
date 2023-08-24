package com.yerayyas.superheromarvelinfo.util

import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse

sealed class SuperheroListState {
    object Loading : SuperheroListState()
    data class Loaded(val superheroes: List<SuperheroItemResponse>) : SuperheroListState()
    object Error : SuperheroListState()
}
