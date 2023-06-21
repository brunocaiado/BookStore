package pt.brunocaiado.bookdetail

import pt.brunocaiado.datalibrary.data.localdata.BookEntity

sealed class BookDetailScreenUiState {

    object Loading : BookDetailScreenUiState()

    data class Book(val bookEntity: BookEntity) : BookDetailScreenUiState()

}