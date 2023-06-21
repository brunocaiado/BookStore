package pt.brunocaiado.booklist

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.brunocaiado.datalibrary.data.localdata.BookEntity
import pt.brunocaiado.datalibrary.data.mediator.RemoteMediatorGenerator
import pt.brunocaiado.datalibrary.data.model.ErrorModel
import pt.brunocaiado.datalibrary.data.repository.BooksRepository
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    remoteMediatorGenerator: RemoteMediatorGenerator,
    private val prettifyErrorModelUseCase: PrettifyErrorModelUseCase
) : ViewModel(), LifecycleEventObserver {

    companion object {
        private const val BOOK_STORE_PAGE_SIZE = 20
    }

    private val _navigationState = MutableStateFlow<BookListNavigationState>(BookListNavigationState.Stay)
    val navigationState: StateFlow<BookListNavigationState> = _navigationState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookListNavigationState.Stay
    )

    private val _bookListState = MutableStateFlow<BookListUiState>(BookListUiState.Loading)
    val bookListState: StateFlow<BookListUiState> = _bookListState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = BookListUiState.Loading
        )

    private val favouriteBookListState: StateFlow<List<String>> =
        booksRepository.getFavoriteListStream()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    private val _mustFilterListState = MutableStateFlow(false)
    private val mustFilterListState: StateFlow<Boolean> = _mustFilterListState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    @OptIn(ExperimentalPagingApi::class)
    val pagingFlow: Flow<PagingData<BookUIModel>> = Pager(
        config = PagingConfig(pageSize = BOOK_STORE_PAGE_SIZE),
        remoteMediator = remoteMediatorGenerator.getBooksRemoteMediator(
            search = "Environment",
        ),
        pagingSourceFactory = {
            booksRepository.getPagingSource()
        }
    ).flow.map { pagingData ->
        pagingData.map { bookEntity ->
            bookEntity.toBookUIModel(getOnBookClick())
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val filteredBookListState: StateFlow<Flow<PagingData<BookUIModel>>?> =
        combine(
            pagingFlow,
            favouriteBookListState,
            mustFilterListState
        ) { bookListState, favouriteBookListState, mustFilterListState ->

            Log.d("bcaiado", "combine bookListState -> ${bookListState}")
            Log.d("bcaiado", "combine favouriteBookListState -> ${favouriteBookListState}")
            Log.d("bcaiado", "combine mustFilterListState -> ${mustFilterListState}")

            Pager(
                config = PagingConfig(pageSize = BOOK_STORE_PAGE_SIZE),
                remoteMediator = remoteMediatorGenerator.getBooksRemoteMediator(
                    search = "Environment",
                ),
                pagingSourceFactory = {
                    booksRepository.getPagingSource()
                }
            ).flow.map { pagingData ->

                if(mustFilterListState){
                    pagingData.filter { model -> favouriteBookListState.firstOrNull { it == model.bookId } != null }.map { bookEntity ->
                        bookEntity.toBookUIModel(getOnBookClick())
                    }
                }else{
                    pagingData.map { bookEntity ->
                        bookEntity.toBookUIModel(getOnBookClick())
                    }

                }

            }.cachedIn(viewModelScope)

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {

            }

            else -> {

            }
        }
    }

    fun onLoadStateListener(loadState: CombinedLoadStates) {
        viewModelScope.launch(Dispatchers.Default) {

            when(val state = loadState.mediator?.refresh){
                is LoadState.Error -> {
                    (state.error as? ErrorModel)?.let { errorModel ->
                        emitErrorModel(errorModel)
                    }
                }
                is LoadState.Loading -> {
                    emitLoading()
                }
                else -> {}
            }

            when(val state = loadState.mediator?.append){
                is LoadState.Error -> {
                    (state.error as? ErrorModel)?.let { errorModel ->
                        emitErrorModel(errorModel)
                    }
                }

                else -> {}
            }


        }

    }

    fun toggleFavoriteFilter(){
        viewModelScope.launch {
            _mustFilterListState.emit(!_mustFilterListState.value)
        }
    }

    private suspend fun emitErrorModel(errorModel: ErrorModel){
        withContext(Dispatchers.Main){
            _bookListState.emit(
                BookListUiState.Error(
                    prettifyErrorModelUseCase.getPrettifiedVersionOfErrorModel(errorModel)
                )
            )
        }
    }

    private suspend fun emitLoading() {
        withContext(Dispatchers.Main) {
            _bookListState.emit(
                BookListUiState.Loading
            )
        }
    }

    private fun getOnBookClick() : (BookEntity?) -> Unit = { bookEntity ->

        bookEntity?.let {
            viewModelScope.launch(Dispatchers.Main) {
                _navigationState.emit(
                    BookListNavigationState.BookDetailScreen(it)
                )
                _navigationState.emit(BookListNavigationState.Stay) // TODO fix this
            }
        }

    }


}