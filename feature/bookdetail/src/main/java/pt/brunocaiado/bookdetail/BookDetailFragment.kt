package pt.brunocaiado.bookdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pt.brunocaiado.bookdetail.databinding.FragmentBookDetailBinding
import pt.brunocaiado.datalibrary.data.localdata.BookEntity

@AndroidEntryPoint
class BookDetailFragment : Fragment() {

    private val viewModel by viewModels<BookDetailViewModel>()

    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(viewModel)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)

        binding.detailFavoriteBtn.setOnClickListener {
            viewModel.toggleFavorite()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.screenState.collect { state ->

                    when(state){
                        is BookDetailScreenUiState.Book -> {
                            showDetail(state.bookEntity)
                        }
                        else -> {

                        }
                    }

                }

            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            Log.i("bcaiado", "Argument=$arguments")

            viewModel.setBookId(it.getString("id"))

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    private fun showDetail(bookEntity: BookEntity){

        binding.detailFavoriteBtn.visibility = View.VISIBLE

        binding.detailTitleTxt.text = bookEntity.volumeInfo?.title
        binding.detailDateTxt.text = bookEntity.volumeInfo?.publishedDate
    }

}