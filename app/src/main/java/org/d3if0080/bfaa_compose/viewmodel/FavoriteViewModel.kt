package org.d3if0080.bfaa_compose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.d3if0080.bfaa_compose.database.FavoriteEntity
import org.d3if0080.bfaa_compose.database.FavoriteRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoriteRepository.getAllFavorites()
}
