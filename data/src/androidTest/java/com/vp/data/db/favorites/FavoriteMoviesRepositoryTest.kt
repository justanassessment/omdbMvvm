package com.vp.data.db.favorites

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vp.data.db.favorites.dao.FavoriteMoviesDao
import com.vp.data.db.favorites.entity.FavoriteMovieEntity
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class FavoriteMoviesRepositoryTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var dao: FavoriteMoviesDao? = null
    private var entity1: FavoriteMovieEntity? = null
    private var entity2: FavoriteMovieEntity? = null
    private var entity3: FavoriteMovieEntity? = null
    private var entity4: FavoriteMovieEntity? = null
    private var entity5: FavoriteMovieEntity? = null

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dao = FavoriteMoviesDatabase.getDatabase(context).favoriteDao()
        entity1 = FavoriteMovieEntity("1", "Poster1")
        entity2 = FavoriteMovieEntity("2", "Poster2")
        entity3 = FavoriteMovieEntity("3", "Poster3")
        entity4 = FavoriteMovieEntity("4", "Poster4")
        entity5 = FavoriteMovieEntity("5", "Poster5")
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllFavoritesTest() {
        val movieEntityExpectedList = ArrayList<FavoriteMovieEntity>()
        val movieEntityActualList = LiveDataTestUtil.getValue(dao!!.getAllFavorites())

        assertThat(movieEntityActualList, `is`<List<FavoriteMovieEntity>>(movieEntityExpectedList))

    }

    @Test
    @Throws(InterruptedException::class)
    fun insertTest() {
        val movieEntityExpectedList = ArrayList<FavoriteMovieEntity>()
        movieEntityExpectedList.add(entity1!!)
        movieEntityExpectedList.add(entity2!!)
        movieEntityExpectedList.add(entity3!!)
        movieEntityExpectedList.add(entity4!!)
        movieEntityExpectedList.add(entity5!!)
        dao!!.insert(entity1!!)
        dao!!.insert(entity2!!)
        dao!!.insert(entity3!!)
        dao!!.insert(entity4!!)
        dao!!.insert(entity5!!)
        val movieEntityActualList = LiveDataTestUtil.getValue(dao!!.getAllFavorites())

        assertThat(movieEntityActualList, `is`<List<FavoriteMovieEntity>>(movieEntityExpectedList))
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteTest() {
        val movieEntityExpectedList = ArrayList<FavoriteMovieEntity>()
        movieEntityExpectedList.add(entity1!!)
        movieEntityExpectedList.add(entity2!!)
        dao!!.insert(entity1!!)
        dao!!.insert(entity2!!)
        dao!!.insert(entity3!!)
        dao!!.delete(entity3!!)
        dao!!.insert(entity4!!)
        dao!!.insert(entity5!!)
        dao!!.delete(entity4!!)
        dao!!.delete(entity5!!)
        val movieEntityActualList = LiveDataTestUtil.getValue(dao!!.getAllFavorites())

        assertThat(movieEntityActualList, `is`<List<FavoriteMovieEntity>>(movieEntityExpectedList))
    }

    @Test
    @Throws(InterruptedException::class)
    fun getFavoriteByIdTest() {
        dao!!.insert(entity1!!)
        dao!!.insert(entity2!!)
        dao!!.insert(entity3!!)
        dao!!.insert(entity4!!)
        dao!!.insert(entity5!!)
        dao!!.delete(entity5!!)

        assertThat(LiveDataTestUtil.getValue(dao!!.getFavoriteById("1")!!), `is`<FavoriteMovieEntity>(entity1))
        assertThat(LiveDataTestUtil.getValue(dao!!.getFavoriteById("2")!!), `is`(not<FavoriteMovieEntity>(entity1)))
        assertThat(LiveDataTestUtil.getValue(dao!!.getFavoriteById("2")!!), `is`(not<FavoriteMovieEntity>(entity3)))
        assertThat(LiveDataTestUtil.getValue(dao!!.getFavoriteById("2")!!), `is`(not<FavoriteMovieEntity>(entity4)))
        assertThat(LiveDataTestUtil.getValue(dao!!.getFavoriteById("2")!!), `is`(not<FavoriteMovieEntity>(entity5)))
        assertThat(LiveDataTestUtil.getValue(dao!!.getFavoriteById("2")!!), `is`<FavoriteMovieEntity>(entity2))
        assertNull(LiveDataTestUtil.getValue(dao!!.getFavoriteById("5")!!))
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteAllTest() {
        dao!!.insert(entity1!!)
        dao!!.insert(entity2!!)
        dao!!.insert(entity3!!)
        dao!!.insert(entity4!!)
        dao!!.insert(entity5!!)
        dao!!.deleteAll()

        assertNull(LiveDataTestUtil.getValue(dao!!.getFavoriteById("1")!!))
        assertNull(LiveDataTestUtil.getValue(dao!!.getFavoriteById("2")!!))
        assertNull(LiveDataTestUtil.getValue(dao!!.getFavoriteById("3")!!))
        assertNull(LiveDataTestUtil.getValue(dao!!.getFavoriteById("4")!!))
        assertNull(LiveDataTestUtil.getValue(dao!!.getFavoriteById("5")!!))

        val movieEntityExpectedList = ArrayList<FavoriteMovieEntity>()
        val movieEntityActualList = LiveDataTestUtil.getValue(dao!!.getAllFavorites())

        assertThat(movieEntityActualList, `is`<List<FavoriteMovieEntity>>(movieEntityExpectedList))

    }

    @After
    fun cleanDb() {
        dao!!.deleteAll()
    }
}