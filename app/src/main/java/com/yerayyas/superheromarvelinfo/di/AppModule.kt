package com.yerayyas.superheromarvelinfo.di

import com.yerayyas.superheromarvelinfo.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule {

    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

//    @Provides
//    fun provideSuperheroRepository(apiService: ApiService): SuperheroRepository {
//        return SuperheroRepository(apiService)
//    }

    // Aquí puedes proporcionar más instancias de clases necesarias
    // Por ejemplo, si necesitas un SuperheroUseCase, puedes proporcionarlo aquí

}
