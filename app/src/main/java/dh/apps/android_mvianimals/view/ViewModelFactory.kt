package dh.apps.android_mvianimals.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dh.apps.android_mvianimals.api.AnimalApi
import dh.apps.android_mvianimals.api.AnimalRepo

class ViewModelFactory(private val api: AnimalApi): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(AnimalRepo(api)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}