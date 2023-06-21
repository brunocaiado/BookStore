package pt.brunocaiado.datalibrary.data.localdata

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object BookVolumeConverter {

    @TypeConverter
    @JvmStatic
    fun fromEntityJson(entity: BookItemVolumeInfoEntity?): String? =
        entity?.let {
            Gson().toJson(entity)
        }


    @TypeConverter
    @JvmStatic
    fun toEntity(entity: String?): BookItemVolumeInfoEntity? {
        val type = object : TypeToken<BookItemVolumeInfoEntity?>() {}.type
        return entity?.let {
            Gson().fromJson(entity, type)
        }
    }

}