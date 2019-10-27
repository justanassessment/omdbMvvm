package com.vp.detail.service

import com.vp.detail.model.MovieDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * An interface for invocation of a Retrofit method that sends a request to a webserver and returns a response.
 * In this case it is sending request to The OMDb API.
 *
 * @param imdbID id of a specified movie
 *
 * @return a Retrofit Call<MovieDetail> that can be executed synchronously or asynchronously
 */
interface DetailService {
    @GET("/")
    fun getMovie(@Query("i") imdbID: String): Call<MovieDetail>
}