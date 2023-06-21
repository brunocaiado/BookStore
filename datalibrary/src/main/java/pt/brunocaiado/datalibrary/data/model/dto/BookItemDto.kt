package pt.brunocaiado.datalibrary.data.model.dto

import com.google.gson.annotations.SerializedName
import pt.brunocaiado.datalibrary.data.localdata.BookEntity
import pt.brunocaiado.datalibrary.data.localdata.BookItemVolumeImageLinksEntity
import pt.brunocaiado.datalibrary.data.localdata.BookItemVolumeInfoEntity

data class BookItemDto(
    @SerializedName("kind") val kind: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("etag") val etag: String?,
    @SerializedName("selfLink") val selfLink: String?,
    @SerializedName("volumeInfo") val volumeInfo: BookItemVolumeInfoDto?,
    //@SerializedName("saleInfo") val saleInfo: saleInfo?,
    //@SerializedName("accessInfo") val accessInfo: accessInfo?,
    //@SerializedName("searchInfo") val searchInfo: searchInfo?,
)

data class BookItemVolumeInfoDto(
    @SerializedName("title") val title: String?,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("publisher") val publisher: String?,
    @SerializedName("publishedDate") val publishedDate: String?,
    @SerializedName("description") val description: String?,
    //@SerializedName("industryIdentifiers") val industryIdentifiers: List<>?,
    //@SerializedName("readingModes") val readingModes: ,
    @SerializedName("pageCount") val pageCount: Int?,
    @SerializedName("printType") val printType: String?,
    @SerializedName("categories") val categories: List<String>?,
    @SerializedName("maturityRating") val maturityRating: String?,
    @SerializedName("allowAnonLogging") val allowAnonLogging: Boolean?,
    @SerializedName("contentVersion") val contentVersion: String?,
    //@SerializedName("panelizationSummary") val panelizationSummary: ,
    @SerializedName("imageLinks") val imageLinks: BookItemVolumeImageLinksDto?,
    @SerializedName("language") val language: String?,
    @SerializedName("previewLink") val previewLink: String?,
    @SerializedName("infoLink") val infoLink: String?,
    @SerializedName("canonicalVolumeLink") val canonicalVolumeLink: String?
)

data class BookItemVolumeImageLinksDto(
    @SerializedName("smallThumbnail") val smallThumbnail: String?,
    @SerializedName("thumbnail") val thumbnail: String,
)

fun BookItemDto.toBookItemEntity(): BookEntity {
    return BookEntity(
        bookId = this.id,
        kind = this.kind,
        etag = this.etag,
        selfLink = this.selfLink,
        volumeInfo = this.volumeInfo?.toBookVolumeEntity()
    )
}

fun List<BookItemDto>.toBookItemEntityList(): List<BookEntity> {
    return this.map {
        it.toBookItemEntity()
    }
}

fun BookItemVolumeInfoDto.toBookVolumeEntity(): BookItemVolumeInfoEntity {
    return BookItemVolumeInfoEntity(
        title = this.title,
        authors = this.authors,
        publisher = this.publisher,
        publishedDate = this.publishedDate,
        description = this.description,
        pageCount = this.pageCount,
        printType = this.printType,
        categories = this.categories,
        maturityRating = this.maturityRating,
        allowAnonLogging = this.allowAnonLogging,
        contentVersion = this.contentVersion,
        language = this.language,
        imageLink = this.imageLinks?.toBookItemVolumeImageLinksEntity(),
        previewLink = this.previewLink,
        infoLink = this.infoLink,
        canonicalVolumeLink = this.canonicalVolumeLink
    )
}

fun BookItemVolumeImageLinksDto.toBookItemVolumeImageLinksEntity(): BookItemVolumeImageLinksEntity{
    return BookItemVolumeImageLinksEntity(
        smallThumbnail = this.smallThumbnail,
        thumbnail = this.thumbnail
    )
}