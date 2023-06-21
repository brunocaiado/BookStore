package pt.brunocaiado.datalibrary.data.localdata

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "book_volume_image_links_entity")
data class BookItemVolumeImageLinksEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val smallThumbnail: String?,
    val thumbnail: String?

): Parcelable

