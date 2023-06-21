package pt.brunocaiado.booklist

import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.request.target.Target
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import pt.brunocaiado.booklist.databinding.ItemBooksBinding

class BookListPagingAdapter: PagingDataAdapter<BookUIModel, RecyclerView.ViewHolder>(
    UI_MODEL_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BooksPagingViewHolder(
            ItemBooksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_books
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)

        (holder as BooksPagingViewHolder).let {
            holder.binding.bookTitle.text = uiModel?.bookEntity?.volumeInfo?.title
        }

        holder.binding.root.setOnClickListener {
            val a = (uiModel?.onClick)
            a?.let {
                it(uiModel.bookEntity)
            }
        }

        uiModel?.bookEntity?.volumeInfo?.imageLink?.thumbnail?.let { url ->

            Log.d("bcaiado", "settingUp Image url -> $url")
            Glide.with(holder.binding.bookImage.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.binding.bookImage.setImageDrawable(null)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
                .into(holder.binding.bookImage)
        } ?: holder.binding.bookImage.setImageDrawable(null)

    }

    companion object {
        private val UI_MODEL_COMPARATOR = object : DiffUtil.ItemCallback<BookUIModel>() {
            override fun areItemsTheSame(
                oldItem: BookUIModel,
                newItem: BookUIModel
            ): Boolean {

                return oldItem.bookEntity == newItem.bookEntity
            }

            override fun areContentsTheSame(
                oldItem: BookUIModel,
                newItem: BookUIModel
            ): Boolean {

                return oldItem.bookEntity.bookId == newItem.bookEntity.bookId
            }
        }
    }
}


class BooksPagingViewHolder(val binding: ItemBooksBinding) : RecyclerView.ViewHolder(binding.root)