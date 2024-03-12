package org.d3if0080.bfaa_compose.api

import org.d3if0080.bfaa_compose.response.DetailUserResponse
import org.d3if0080.bfaa_compose.response.ListFollowResponse
import org.d3if0080.bfaa_compose.response.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun search(
        @Query("q") username: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun detailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun followers(
        @Path("username") username: String
    ): Call<ArrayList<ListFollowResponse>>

    @GET("users/{username}/following")
    fun following(
        @Path("username") username: String
    ): Call<ArrayList<ListFollowResponse>>
}