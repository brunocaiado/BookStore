package pt.brunocaiado.datalibrary.data.localdata

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        BookEntity::class,
        BookItemVolumeInfoEntity::class,
        BookItemVolumeImageLinksEntity::class,
        BookFavoriteEntity::class ],
    version = 1
)
@TypeConverters(
    BookVolumeConverter::class,
    BookVolumeImageLinksConverter::class,
    ListStringConverter::class
)
abstract class StoreDatabase: RoomDatabase() {
    abstract val booksDao: BooksDao
}