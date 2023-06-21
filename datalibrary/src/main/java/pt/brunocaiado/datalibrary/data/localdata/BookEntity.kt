package pt.brunocaiado.datalibrary.data.localdata

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import pt.brunocaiado.datalibrary.data.model.dto.BookItemVolumeInfoDto

@Parcelize
@Entity(tableName = "book_entity")
@TypeConverters(BookVolumeConverter::class)
data class BookEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookId: String?,
    val kind: String?,

    val etag: String?,
    val selfLink: String?,
    @TypeConverters(BookVolumeConverter::class)
    val volumeInfo: BookItemVolumeInfoEntity?

):Parcelable