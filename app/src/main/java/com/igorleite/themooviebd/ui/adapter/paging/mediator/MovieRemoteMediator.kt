package com.igorleite.themooviebd.ui.adapter.paging.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.igorleite.themooviebd.data.RequestState
import com.igorleite.themooviebd.data.local.MovieDataBase
import com.igorleite.themooviebd.data.model.entities.KeysEntity
import com.igorleite.themooviebd.data.model.entities.MovieEntity
import com.igorleite.themooviebd.domain.remote.GetMoviesByName
import com.igorleite.themooviebd.utils.Constants
import com.igorleite.themooviebd.utils.fromDomainsToEntities

/** This function implements paging 3
 * Its works with mediator.
 * performs paging, with local cache.
 ** @param type specific type of search "movie, series, episode"
 ** @param search title for search in api
 ** @param database provide a database instance injected by hilt
 ** @param useCase provide a useCase instance injected by hilt
 * @author Igorhleite
 **/
@ExperimentalPagingApi
class MovieRemoteMediator
    (
    private val useCase: GetMoviesByName,
    private val database: MovieDataBase,
    private val type: String,
    private val search: String
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> return pageKeyData
            else -> pageKeyData as Int
        }
        try {
            val response = useCase.run(
                GetMoviesByName.Params(
                    type = type,
                    search = search,
                    page = page
                )
            )
            /**
             * moviesList -> remotely retrieved data list
             * */
            val moviesList =
                if (response is RequestState.ResponseSuccess) response.value else emptyList()
            val isEndOfList = moviesList.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.movieDao().deleteAllMovies()
                    database.remoteKeysDao().deleteAllKeys()
                }
                /**
                 * Constants.PAGE_INDEX -> provides which is the first page to get from the api (conventionally it is 1)
                 * */
                val prevKey = if (page == Constants.PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = moviesList.map {
                    KeysEntity(
                        it.imdbID,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                database.remoteKeysDao().insertAllKeys(keys)
                database.movieDao().insertAllMovies(moviesList.fromDomainsToEntities())
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                return remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        }
    }

    // get the closest remote key inserted which had the data
    private suspend fun getClosestRemoteKey(state: PagingState<Int, MovieEntity>): KeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.imdbID?.let { repoId ->
                database.remoteKeysDao().getRemoteKesById(repoId)
            }
        }
    }

    // get the last remote key inserted which had the data
    private suspend fun getLastRemoteKey(state: PagingState<Int, MovieEntity>): KeysEntity? {
        return state.pages
            .lastOrNull {
                it.data.isNotEmpty()
            }
            ?.data?.lastOrNull()
            ?.let { movie ->
                database.remoteKeysDao().getRemoteKesById(movie.imdbID)
            }
    }

    // get the first remote key inserted which had the data
    private suspend fun getFirstRemoteKey(state: PagingState<Int, MovieEntity>): KeysEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie ->
                database.remoteKeysDao().getRemoteKesById(movie.imdbID)
            }
    }
}
