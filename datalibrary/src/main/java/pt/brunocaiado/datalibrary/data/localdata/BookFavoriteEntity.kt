package pt.brunocaiado.datalibrary.data.localdata

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_favorite_entity")
data class BookFavoriteEntity(
    @PrimaryKey
    val bookId: String
)
