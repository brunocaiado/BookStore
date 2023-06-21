package pt.brunocaiado.datalibrary.data.remotedatasource

import pt.brunocaiado.datalibrary.data.ApiPerformer
import pt.brunocaiado.datalibrary.data.ApiResult
import pt.brunocaiado.datalibrary.data.api.BookStoreApi
import pt.brunocaiado.datalibrary.data.model.dto.BookStoreApiResultDto
import javax.inject.Inject

class BookStoreRemoteDataSource @Inject constructor(
    private val bookStoreApi: BookStoreApi
) {

    suspend fun getBooks(
        search: String,
        startIndex: Int = 0,
        maxResults: Int = 20,
    ): ApiResult<BookStoreApiResultDto> {

        return ApiPerformer<BookStoreApiResultDto>().performSafeCall {
            bookStoreApi.getBooks(
                headers = mapOf(),
                queries = hashMapOf(
                    BookStoreApi.BOOKS_API_QUERY_SEARCH to search,
                    BookStoreApi.BOOKS_API_QUERY_START_INDEX to startIndex.toString(),
                    BookStoreApi.BOOKS_API_QUERY_MAX_RESULTS to maxResults.toString()
                )
            )

        }

    }
}