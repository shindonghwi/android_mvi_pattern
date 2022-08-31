package dh.apps.android_mvianimals.api

import dh.apps.android_mvianimals.model.Animal
import retrofit2.http.GET

interface AnimalApi {
    @GET("animals.json")
    suspend fun getAnimals(): List<Animal>
}