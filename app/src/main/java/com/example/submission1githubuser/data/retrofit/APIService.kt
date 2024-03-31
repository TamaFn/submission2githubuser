package com.example.submission1githubuser.data.retrofit

import com.example.submission1githubuser.data.response.DetailUser
import com.example.submission1githubuser.data.response.GithubUser
import com.example.submission1githubuser.data.response.SearchUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("users")
    fun getUsers(
        @Header("Authorization") token: String
    ): Call<List<GithubUser>>

    @GET("search/users")
    fun getUserSearch(
        @Query("q") login: String,
    ): Call<SearchUsers>

    @GET("users/{login}")
    fun getDetailUser(
        @Path("login") login: String,
    ): Call<DetailUser>

    @GET("users/{login}/followers")
    fun getAllFollowers(
        @Path("login") login: String,
    ): Call<List<GithubUser>>

    //memanggil list following
    @GET("users/{login}/following")
    fun getAllFollowings(
        @Path("login") login: String,
    ): Call<List<GithubUser>>
}