package com.chocomiruku.myhome.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.databinding.ChatListItemBinding
import com.chocomiruku.myhome.domain.models.Message
import com.chocomiruku.myhome.util.UserRole
import com.chocomiruku.myhome.util.convertToDateString

class MessageAdapter(
    private val currentUserId: String
) :
    ListAdapter<Message, MessageAdapter.ViewHolder>(MessageDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(
            message,
            currentUserId
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(
        private val binding: ChatListItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            message: Message,
            currentUserId: String
        ) = with(binding) {
            message.sender?.let { sender ->
                val isCurrentUser = sender.uid == currentUserId
                othersGroup.isVisible = !isCurrentUser
                messageCardCurrentUser.isVisible = isCurrentUser
                if (isCurrentUser) bindCurrentUserMessage(message) else bindOtherUserMessage(message)
            }
        }

        private fun bindOtherUserMessage(message: Message) = with(binding) {
            val user = message.sender
            user?.let {
                messageTextOtherUser.text = message.text
                timeTextOtherUser.text = message.date.convertToDateString()

                nameTextOtherUser.text = when (user.role) {
                    UserRole.ADMIN -> "${user.name} (Администратор)"
                    UserRole.MODERATOR -> "${user.name} (Модератор)"
                    else -> user.name
                }

                if (user.imageUri != null) {
                    defaultImageText.isVisible = false
                    loadProfileImage(user.imageUri)
                } else {
                    defaultImageText.isVisible = true
                    profileImage.setBackgroundColor(user.defaultColorId)
                    defaultImageText.text =
                        user.name.first().uppercase()
                }

                profileImage.setOnClickListener {
                    it.findNavController().navigate(
                        ChatFragmentDirections.actionChatFragmentToUserProfileFragment(
                        ).setUser(user)
                    )
                }

                message.imageUri?.let { uriString ->
                    loadMessageImage(uriString, messageImageOtherUser)
                }
            }
        }

        private fun bindCurrentUserMessage(message: Message) = with(binding) {
            messageTextCurrentUser.text = message.text
            timeTextCurrentUser.text = message.date.convertToDateString()

            message.imageUri?.let { uriString ->
                loadMessageImage(uriString, messageImageCurrentUser)
            }
        }

        private fun loadProfileImage(uriString: String) = with(binding) {
            val uri = uriString.toUri()

            Glide.with(profileImage.context)
                .load(uri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_anim)
                        .error(R.drawable.ic_broken_image)
                )
                .into(profileImage)
        }

        private fun loadMessageImage(uriString: String, view: ImageView) {
            val uri = uriString.toUri()
            view.isVisible = true

            Glide.with(view.context)
                .load(uri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_anim)
                        .error(R.drawable.ic_broken_image)
                )
                .into(view)
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ChatListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class MessageDiffCallback :
        DiffUtil.ItemCallback<Message>() {

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.messageId == newItem.messageId
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}