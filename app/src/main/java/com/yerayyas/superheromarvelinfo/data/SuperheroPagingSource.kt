package com.yerayyas.superheromarvelinfo.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yerayyas.superheromarvelinfo.data.model.SuperheroItemResponse
import com.yerayyas.superheromarvelinfo.network.ApiService
import com.yerayyas.superheromarvelinfo.util.Constants.API_KEY
import com.yerayyas.superheromarvelinfo.util.Constants.HASH
import com.yerayyas.superheromarvelinfo.util.Constants.TS

class SuperheroPagingSource(
    private val apiService: ApiService,
    private val query: String
) :
    PagingSource<Int, SuperheroItemResponse>() {
    override fun getRefreshKey(state: PagingState<Int, SuperheroItemResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SuperheroItemResponse> {
        return try {
            val page = params.key ?: 0
            val offset = page * PAGE_SIZE
            val response = apiService.getSuperheroes(offset = offset, API_KEY, HASH, TS)
            val nextKey = if (offset >= (response.body()?.data?.total!!)) null else page + 1
            return LoadResult.Page(
                data = response.body()?.data!!.superheroes,
                prevKey = null, // Only paging forward.
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
