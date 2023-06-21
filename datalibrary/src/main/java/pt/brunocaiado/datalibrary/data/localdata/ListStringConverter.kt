package pt.brunocaiado.datalibrary.data.localdata

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ListStringConverter {

    @TypeConverter
    @JvmStatic
    fun fromListJson(list: List<String>?): String? =
        list?.let {
            Gson().toJson(list)
        }

    @TypeConverter
    @JvmStatic
    fun toList(jsonStrings: String?): List<String>?{
        val type = object : TypeToken<List<String>?>() {}.type
        return jsonStrings?.let {
            Gson().fromJson(jsonStrings, type)
        }
    }

}