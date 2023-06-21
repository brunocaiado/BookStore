package pt.brunocaiado.booklist

import pt.brunocaiado.datalibrary.data.localdata.BookEntity
import pt.brunocaiado.datalibrary.data.model.dto.BookStoreApiResultDto

sealed class BookListUiState {

    object Loading : BookListUiState()

    data class Error(val errorUiModel: ErrorUiModel) : BookListUiState()

    data class BookList(val bookStoreApiResultDto: BookStoreApiResultDto) : BookListUiState()

}

sealed class BookListNavigationState{

    object Stay: BookListNavigationState()

    data class BookDetailScreen(
        val bookEntity: BookEntity
    ) : BookListNavigationState()
}

data class BookUIModel(
    var bookEntity: BookEntity,
    val onClick: (BookEntity?) -> Unit
)

fun BookEntity.toBookUIModel(onClick: (BookEntity?) -> Unit): BookUIModel {
    return BookUIModel(
        bookEntity = this,
        onClick = onClick
    )
}


data class ErrorUiModel(
    val title: String?,
    val message: String?
)