package pt.brunocaiado.datalibrary.data.repository

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pt.brunocaiado.datalibrary.data.ApiResult
import pt.brunocaiado.datalibrary.data.localdata.BookEntity
import pt.brunocaiado.datalibrary.data.localdata.BookFavoriteEntity
import pt.brunocaiado.datalibrary.data.localdata.BooksDao
import pt.brunocaiado.datalibrary.data.model.dto.BookStoreApiResultDto
import pt.brunocaiado.datalibrary.data.remotedatasource.BookStoreRemoteDataSource
import javax.inject.Inject

interface BooksRepository {

    suspend fun getBooks(search: String) : Flow<BookStoreApiResultDto?>

    fun getPagingSource() : PagingSource<Int, BookEntity>

    suspend fun getBookById(bookId: String) : BookEntity?

    suspend fun setBookFavorite(bookId:String)

    fun getFavoriteListStream():Flow<List<String>>
}

class BooksRepositoryImpl @Inject constructor(
    private val bookStoreRemoteDataSource: BookStoreRemoteDataSource,
    private val booksDao: BooksDao,
): BooksRepository {

    override suspend fun getBooks(search: String) : Flow<BookStoreApiResultDto?> {

        return flow {

            when(val result = bookStoreRemoteDataSource.getBooks(search)){
                is ApiResult.Success -> {
                    emit(result.data)
                }

                is ApiResult.Error -> {
                    emit(null)
                }
            }


        }

    }

    override fun getPagingSource(): PagingSource<Int, BookEntity>{
        return booksDao.pagingSource()
    }

    override suspend fun getBookById(bookId: String): BookEntity?{
        return booksDao.getBookById(bookId = bookId)
    }

    override suspend fun setBookFavorite(bookId: String) {
        booksDao.setBookFavorite(
            BookFavoriteEntity(
                bookId = bookId
            )
        )
    }

    override fun getFavoriteListStream():Flow<List<String>> {
        return booksDao.getFavouriteChannelsStream()
    }

    private fun buildPrimaryKey(country: String, category: String): String{
        return "$country$category"
    }

}