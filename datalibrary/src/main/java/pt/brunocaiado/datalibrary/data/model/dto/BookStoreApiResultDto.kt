package pt.brunocaiado.datalibrary.data.model.dto

import com.google.gson.annotations.SerializedName

data class BookStoreApiResultDto(
    @SerializedName("kind") val kind: String?,
    @SerializedName("totalItems") val totalItems: Int?,
    @SerializedName("items") val items: List<BookItemDto>?

)


