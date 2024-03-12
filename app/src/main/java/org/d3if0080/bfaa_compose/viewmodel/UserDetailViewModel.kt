package org.d3if0080.bfaa_compose.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.d3if0080.bfaa_compose.response.DetailUserResponse
import org.d3if0080.bfaa_compose.api.ApiConfig
import org.d3if0080.bfaa_compose.database.FavoriteEntity
import org.d3if0080.bfaa_compose.database.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {

    var userDetail by mutableStateOf<DetailUserResponse?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val mFavoriteRepository: FavoriteRepository =
        FavoriteRepository(application)

    fun insert(user: FavoriteEntity) {
        mFavoriteRepository.insert(user)
    }

    fun delete(id: Int) {
        mFavoriteRepository.delete(id)
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoriteRepository.getAllFavorites()


    fun detailUser(context: Context, username: String) {
        try {
            isLoading = true
            val client = ApiConfig.getApiService().detailUser(username)
            client.enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    isLoading = false
                    if (response.isSuccessful) {
                        userDetail = response.body()
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    isLoading = false
                    showToast(context,"Failed to fetch user details. Please try again.")
                }
            })
        } catch (e: Exception) {
            showToast(context, "An unexpected error occurred. Please try again.")
        }
    }

    private fun showToast(context: Context,message: String) {
        context.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }
}