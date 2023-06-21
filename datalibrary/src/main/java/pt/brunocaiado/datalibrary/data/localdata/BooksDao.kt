package pt.brunocaiado.datalibrary.data.localdata

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {

    @Upsert
    suspend fun upsertAll(beers: List<BookEntity>)

    @Query("DELETE FROM book_entity")
    suspend fun clearAll()

    @Query("SELECT * FROM book_entity")
    fun pagingSource(): PagingSource<Int, BookEntity>

    @Query("SELECT COUNT() FROM book_entity")
    suspend fun getCount(): Int

    @Query("SELECT * FROM book_entity WHERE book_entity.bookId = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setBookFavorite(bookFavoriteEntity: BookFavoriteEntity)

    @Query("SELECT * FROM book_favorite_entity")
    fun getFavouriteChannelsStream(): Flow<List<String>>

}