package com.example.submission1githubuser.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submission1githubuser.BuildConfig
import com.example.submission1githubuser.data.repository.UserRepository
import com.example.submission1githubuser.data.response.DetailUser
import com.example.submission1githubuser.data.response.GithubUser
import com.example.submission1githubuser.data.retrofit.ApiConfig
import com.example.submission1githubuser.ui.Activity.DetailActivity.Companion.EXTRA_USER
import retrofit2.Call
import retrofit2.Response

class DetailViewModel(mAppk: Application) : ViewModel() {

    private val _detailuser = MutableLiveData<DetailUser>()
    val detailuser: LiveData<DetailUser> = _detailuser

    private val _allfollowers = MutableLiveData<List<GithubUser>>()
    val allfollowers: LiveData<List<GithubUser>> = _allfollowers

    private val _allfollowings = MutableLiveData<List<GithubUser>>()
    val allfollowings: LiveData<List<GithubUser>> = _allfollowings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingFollower = MutableLiveData<Boolean>()
    val isLoadingFollower: LiveData<Boolean> = _isLoadingFollower

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    private val UserData: UserRepository = UserRepository(mAppk)

    fun getAllUsers(): LiveData<List<GithubUser>> = UserData.getAllData()
    fun insertDataUser(user: GithubUser) {
        UserData.insertUser(user)
    }

    fun deleteDataUser(user: GithubUser) {
        UserData.deleteUser(user)
    }


    fun setUserLogin(userLogin: String) {
        getDetailUser(userLogin)
        getDetailUserFollowers(userLogin)
        getDetailUserFollowings(userLogin)
    }


    private fun getDetailUser(userLogin: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getDetailUser(userLogin)
        api.enqueue(object : retrofit2.Callback<DetailUser> {
            override fun onResponse(call: Call<DetailUser>, response: Response<DetailUser>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailuser.value = responseBody!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getDetailUserFollowers(userLogin: String) {
        _isLoadingFollower.value = true
        val api = ApiConfig.getApiService().getAllFollowers(userLogin)
        api.enqueue(object : retrofit2.Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>
            ) {
                _isLoadingFollower.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _allfollowers.value = responseBody!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _isLoadingFollower.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getDetailUserFollowings(userLogin: String) {
        _isLoadingFollowing.value = true
        val api = ApiConfig.getApiService().getAllFollowings(userLogin)
        api.enqueue(object : retrofit2.Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>
            ) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _allfollowings.value = responseBody!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    companion object {
        private const val TAG = "DetailViewModel"
    }
}