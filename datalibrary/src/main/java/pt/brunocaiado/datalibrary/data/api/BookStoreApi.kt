package pt.brunocaiado.datalibrary.data.api

import pt.brunocaiado.datalibrary.data.model.dto.BookStoreApiResultDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap

interface BookStoreApi {

    @GET("books/v1/volumes")
    suspend fun getBooks(
        @HeaderMap headers: Map<String, String>,
        @QueryMap queries: Map<String, String>
    ): Response<BookStoreApiResultDto>

    companion object {
        const val BASE_URL = "https://www.googleapis.com/"

        const val BOOKS_API_QUERY_SEARCH = "q"
        const val BOOKS_API_QUERY_MAX_RESULTS = "maxResults"
        const val BOOKS_API_QUERY_START_INDEX = "startIndex"

    }

}