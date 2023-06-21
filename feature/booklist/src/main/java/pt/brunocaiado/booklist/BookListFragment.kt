package pt.brunocaiado.booklist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pt.brunocaiado.booklist.databinding.FragmentBookListBinding
import pt.brunocaiado.datalibrary.data.localdata.BookEntity

@AndroidEntryPoint
class BookListFragment : Fragment() {

    private val viewModel by viewModels<BookListViewModel>()

    private var _binding: FragmentBookListBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookListAdapter: BookListPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookListBinding.inflate(inflater, container, false)

        binding.bookFavoriteFilterBtn.setOnClickListener {
            viewModel.toggleFavoriteFilter()
        }

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.filteredBookListState.collectLatest { pagingFlow ->

                Log.d(this@BookListFragment.tag, "pagingFlow ")

                setupRecyclerView()

                viewLifecycleOwner.lifecycleScope.launch {

                    viewModel.pagingFlow.collectLatest { pagingData ->
                        Log.d(this@BookListFragment.tag, "submitData ")

                        showBookList()

                        bookListAdapter.submitData(pagingData)
                    }

                }

            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookListAdapter.loadStateFlow.collect { state ->
                    if (state.source.refresh is LoadState.Loading) {
                        showLoading()
                    } else {
                        showBookList()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {

                viewModel.navigationState.collectLatest { state ->

                    when (state) {
                        is BookListNavigationState.BookDetailScreen -> {
                            navigateToBookDetailScreen(state.bookEntity)
                        }
                        else -> {

                        }

                    }

                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(){

        bookListAdapter = BookListPagingAdapter().apply {
            addLoadStateListener {
                viewModel.onLoadStateListener(it)
            }

        }

        binding.bookList.adapter = bookListAdapter
        binding.bookList.layoutManager = GridLayoutManager(context, 2)


    }

    private fun showLoading(){

        Log.d(this.tag, "showLoading()")

        binding.bookList.visibility = View.GONE
        //binding.errorScreen.visibility = View.GONE
        binding.bookListLoading.visibility = View.VISIBLE
    }

    private fun showBookList(){

        Log.d(this.tag, "showBookList()")

        binding.bookListLoading.visibility = View.GONE
        //binding.errorScreen.visibility = View.GONE
        binding.bookList.visibility = View.VISIBLE
    }


    private fun showErrorScreen(errorUiModel: ErrorUiModel){
        binding.bookListLoading.visibility = View.GONE
        binding.bookList.visibility = View.GONE

        //binding.errorScreen.visibility = View.VISIBLE
    }

    private fun navigateToBookDetailScreen(bookEntity: BookEntity){
        val request = NavDeepLinkRequest.Builder
            .fromUri("bookStore://bookstore.com/detail?bookId={id}".replace("{id}", bookEntity.bookId?:"").toUri())
            .build()
        findNavController().navigate(request)

    }


}