package com.vp.list.viewmodel

import com.vp.list.model.ListItem
import java.util.*

class SearchResult private constructor(val items: List<ListItem>, val totalResult: Int, val listState: ListState) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SearchResult?
        return totalResult == that!!.totalResult &&
                items == that.items &&
                listState === that.listState
    }

    override fun hashCode(): Int {
        return Objects.hash(items, totalResult, listState)
    }

    companion object {

        fun error(): SearchResult {
            return SearchResult(emptyList(), 0, ListState.ERROR)
        }

        fun success(items: List<ListItem>, totalResult: Int): SearchResult {
            return SearchResult(items, totalResult, ListState.LOADED)
        }

        fun inProgress(): SearchResult {
            return SearchResult(emptyList(), 0, ListState.IN_PROGRESS)
        }
    }
}
