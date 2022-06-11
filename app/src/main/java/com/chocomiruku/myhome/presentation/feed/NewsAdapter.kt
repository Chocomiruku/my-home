package com.chocomiruku.myhome.presentation.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.databinding.NewsListItemBinding
import com.chocomiruku.myhome.domain.models.News
import com.chocomiruku.myhome.util.convertToDateString
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NewsAdapter(
    private val isAdmin: Boolean?,
    private val onDelete: (newsId: String) -> Unit
) :
    ListAdapter<News, NewsAdapter.ViewHolder>(NewsDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(isAdmin, news, onDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(
        private val binding: NewsListItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            isAdmin: Boolean?,
            news: News,
            onDelete: (newsId: String) -> Unit
        ) = with(binding) {
            titleText.text = news.title
            newsText.text = news.text
            dateText.text = news.date.convertToDateString()
            isAdmin?.let {
                editBtn.apply {
                    isVisible = it
                    setOnClickListener {
                        findNavController().navigate(
                            NewsFeedFragmentDirections.actionNewsFeedFragmentToAddEditNewsFragment(
                                news
                            )
                        )
                    }
                }
                deleteBtn.apply {
                    isVisible = it
                    setOnClickListener {
                        showAlertDialog {
                            onDelete(news.newsId)
                        }
                    }
                }
            }

            news.imageUri?.let { uriString ->
                newsImage.isVisible = true
                loadImage(uriString)
            } ?: run {
                newsImage.isVisible = false
            }
        }

        private fun loadImage(uriString: String) = with(binding) {
            val uri = uriString.toUri()

            Glide.with(newsImage.context)
                .load(uri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_anim)
                        .error(R.drawable.ic_broken_image)
                )
                .into(newsImage)
        }

        private fun showAlertDialog(onAccept: () -> Unit) {
            MaterialAlertDialogBuilder(binding.root.context)
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.delete_question)
                .setNeutralButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(R.string.accept) { _, _ ->
                    onAccept()
                }
                .show()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    NewsListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class NewsDiffCallback :
        DiffUtil.ItemCallback<News>() {

        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.newsId == newItem.newsId
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }
}