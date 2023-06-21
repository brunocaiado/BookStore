package pt.brunocaiado.datalibrary.data.localdata

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "book_volume_entity")
@TypeConverters(BookVolumeImageLinksConverter::class, ListStringConverter::class)
data class BookItemVolumeInfoEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String?,
    @TypeConverters(ListStringConverter::class)
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val printType: String?,
    @TypeConverters(ListStringConverter::class)
    val categories: List<String>?,
    val maturityRating: String?,
    val allowAnonLogging: Boolean?,
    val contentVersion: String?,
    val language: String?,
    @TypeConverters(BookVolumeImageLinksConverter::class)
    val imageLink: BookItemVolumeImageLinksEntity?,
    val previewLink: String?,
    val infoLink: String?,
    val canonicalVolumeLink: String?

): Parcelable
