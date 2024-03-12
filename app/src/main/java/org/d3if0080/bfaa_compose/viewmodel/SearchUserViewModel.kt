package org.d3if0080.bfaa_compose.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if0080.bfaa_compose.response.ItemsItem
import org.d3if0080.bfaa_compose.response.SearchUserResponse
import org.d3if0080.bfaa_compose.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("MutableCollectionMutableState")
class SearchUserViewModel : ViewModel() {

    private val _searchList = MutableLiveData<ArrayList<ItemsItem>?>(null)
    val searchList: LiveData<ArrayList<ItemsItem>?> = _searchList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var savedSearchList: ArrayList<ItemsItem>? = null

    fun searchUser(context: Context, username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().search(username)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _searchList.value = ArrayList(responseBody.items)
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                showToast(context, "An unexpected error occurred. Please try again.")
            }
        })
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun saveState() {
        savedSearchList = _searchList.value
    }

    fun restoreState() {
        _searchList.value = savedSearchList
    }

    override fun onCleared() {
        saveState()
        super.onCleared()
    }
}
