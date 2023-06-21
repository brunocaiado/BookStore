package pt.brunocaiado.datalibrary.data.localdata

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object BookVolumeImageLinksConverter {

    @TypeConverter
    @JvmStatic
    fun fromEntityJson(entity: BookItemVolumeImageLinksEntity?): String? =
        entity?.let {
            Gson().toJson(entity)
        }


    @TypeConverter
    @JvmStatic
    fun toEntity(entity: String?): BookItemVolumeImageLinksEntity? {
        val type = object : TypeToken<BookItemVolumeImageLinksEntity?>() {}.type
        return entity?.let {
            Gson().fromJson(entity, type)
        }
    }

}