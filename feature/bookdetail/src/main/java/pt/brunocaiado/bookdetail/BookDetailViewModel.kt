package pt.brunocaiado.bookdetail

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.brunocaiado.datalibrary.data.localdata.BookEntity
import pt.brunocaiado.datalibrary.data.repository.BooksRepository
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val booksRepository: BooksRepository
): ViewModel(), LifecycleEventObserver {

    private var _bookEntity: BookEntity? = null
    var bookEntity: BookEntity?
        get() = _bookEntity
        set(value) {
            _bookEntity = value
        }

    private var _bookId: String? = null

    private val _screenState = MutableStateFlow<BookDetailScreenUiState>(BookDetailScreenUiState.Loading)
    val screenState: StateFlow<BookDetailScreenUiState> = _screenState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = BookDetailScreenUiState.Loading
        )

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {

                _bookId?.let { id ->
                    getBookDetail(id)
                }

                /*bookEntity?.let { bookEntity ->
                    viewModelScope.launch(Dispatchers.Main) {
                        _screenState.emit(
                            BookDetailScreenUiState.Book(bookEntity)
                        )
                    }
                }*/

            }

            else -> {

            }
        }
    }

    private fun getBookDetail(bookId:String) {
        viewModelScope.launch(Dispatchers.IO) {
            booksRepository.getBookById(bookId)?.let { bookEntity ->
                withContext(Dispatchers.Main){
                    _bookEntity = bookEntity
                    _screenState.emit(
                        BookDetailScreenUiState.Book(bookEntity)
                    )
                }
            }
        }
    }

    fun toggleFavorite(){
        _bookEntity?.bookId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                Log.d("bcaiado", "setBookFavorite id -> ${id}")

                booksRepository.setBookFavorite(id)
            }
        }
    }

    fun setBookId(bookId: String?){
        this._bookId = bookId
    }
}