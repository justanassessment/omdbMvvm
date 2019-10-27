package com.vp.detail

interface QueryProvider {
    /**
     * Returns imdbID of a movie
     */
    fun getMovieId(): String
}