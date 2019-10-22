package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import retrofit2.mock.Calls
import java.io.IOException

class ListViewModelTest {

    @JvmField
    @Rule
    var instantTaskRule = InstantTaskExecutorRule()

    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService = mock(SearchService::class.java)
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure(IOException()))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle(TITLE, 1)

        //then
        assertThat(listViewModel.observeMovies().value!!.listState).isEqualTo(ListState.ERROR)
    }

    @Test
    fun shouldReturnInProgressState() {
        //given
        val searchService = mock(SearchService::class.java)
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse::class.java)))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle(TITLE, 1)

        //then
        verify(mockObserver).onChanged(SearchResult.inProgress())
    }

    @Test
    fun shouldReturnSuccessState() {
        //given
        val searchService = mock(SearchService::class.java)
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse::class.java)))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle(TITLE, 1)

        //then
        verify(mockObserver).onChanged(
            SearchResult.success(
                listViewModel.observeMovies().value!!.items,
                listViewModel.observeMovies().value!!.totalResult
            )
        )
    }

    companion object {
        private const val TITLE = "Title example"
    }
}