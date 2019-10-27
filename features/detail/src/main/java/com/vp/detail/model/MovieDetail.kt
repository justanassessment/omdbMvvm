package com.vp.detail.model

import com.google.gson.annotations.SerializedName

/**
 * Model of a movie details
 *
 * @param title
 * @param year
 * @param runtime
 * @param director
 * @param plot
 * @param poster
 */
data class MovieDetail(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Runtime") val runtime: String,
    @SerializedName("Director") val director: String,
    @SerializedName("Plot") val plot: String,
    @SerializedName("Poster") val poster: String
)