package pt.brunocaiado.datalibrary.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import pt.brunocaiado.datalibrary.data.ApiResult
import pt.brunocaiado.datalibrary.data.localdata.BookEntity
import pt.brunocaiado.datalibrary.data.localdata.BooksDao
import pt.brunocaiado.datalibrary.data.localdata.StoreDatabase
import pt.brunocaiado.datalibrary.data.model.ErrorModel
import pt.brunocaiado.datalibrary.data.model.dto.BookStoreApiResultDto
import pt.brunocaiado.datalibrary.data.model.dto.toBookItemEntityList
import pt.brunocaiado.datalibrary.data.remotedatasource.BookStoreRemoteDataSource


@OptIn(ExperimentalPagingApi::class)
class BooksRemoteMediator(
    private val search: String,
    private val bookStoreRemoteDataSource: BookStoreRemoteDataSource,
    private val storeDatabase: StoreDatabase,
    private val booksDao: BooksDao
): RemoteMediator<Int, BookEntity>() {

    //private var pageCount = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookEntity>
    ): MediatorResult {

            val loadKey = when (loadType) {

                LoadType.REFRESH -> {
                    Log.d("bcaiado", "REFRESH state.pages -> ${state.pages.size}")
                    0
                }

                LoadType.PREPEND -> {
                    Log.d("bcaiado", "PREPEND state.pages -> ${state.pages.size}")
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                LoadType.APPEND -> {

                    Log.d("bcaiado", "APPEND pageSize -> ${state.config.pageSize}")
                    Log.d("bcaiado", "APPEND state.pages -> ${state.pages.size}")

                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        0
                    } else {
                        state.pages.size + 1
                    }

                }
            }

        return loadKey.let {
            when(val result = bookStoreRemoteDataSource.getBooks(
                search = search,
                startIndex = loadKey * 20,
                maxResults = state.config.pageSize
            )){
                is ApiResult.Success -> {
                    //pageCount += 1
                    val books = result.data.items?.toBookItemEntityList() ?: emptyList()

                    val transactionDone = storeDatabase.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            booksDao.clearAll()
                        }

                        booksDao.upsertAll(books)

                        true
                    }

                    books.forEach {
                        Log.d("bcaiado", "upsert ${it.bookId}")
                    }

                    return if (transactionDone) {

                        val endOfPaginationReached = loadKey >= (result.data.totalItems?.div(state.config.pageSize))!!

                        Log.d("bcaiado", "endOfPaginationReached -> $endOfPaginationReached")
                        Log.d("bcaiado", "bookStoreApiResultDto.totalItems -> ${result.data.totalItems}")
                        Log.d("bcaiado", "state.config.pageSize -> ${state.config.pageSize}")
                        Log.d("bcaiado", "div -> ${result.data.totalItems?.div(state.config.pageSize)}")
                        Log.d("bcaiado", "loadKey -> ${loadKey}")

                        MediatorResult.Success(
                            endOfPaginationReached = result.data.items.isNullOrEmpty()
                        )

                    } else {

                        Log.d("bcaiado", "MediatorResult.Error")

                        MediatorResult.Error(
                            ErrorModel(
                                errorCode = "",
                                errorMessage = "RemoteMediator failed on withTransaction"
                            )
                        )
                    }

                }

                is ApiResult.Error -> {
                    MediatorResult.Error(result.error)
                }
            }
        }


    }

}