package com.chocomiruku.myhome.data.repository

import android.util.Log
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.Message
import com.chocomiruku.myhome.domain.repository.MessageRepository
import com.chocomiruku.myhome.domain.repository.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userRepo: UserRepository
) : MessageRepository {

    private val messagesCollectionRef: CollectionReference
        get() = firestore.collection("messages")


    override suspend fun sendMessage(message: Message) {
        try {
            messagesCollectionRef.add(message)
        } catch (e: Exception) {
            Log.d(TAG, "sendingMessage:failure", e)
        }
    }

    @OptIn(FlowPreview::class)
    override fun getMessages(): Flow<Resource<List<Message>>> = callbackFlow {
        val listener =
            messagesCollectionRef.addSnapshotListener { querySnapshot, firebaseException ->
                if (firebaseException != null) {
                    Log.e(TAG, "addingListener:failure", firebaseException)
                    return@addSnapshotListener
                }

                val messagesList = mutableListOf<Message>()
                querySnapshot?.documents?.forEach { documentSnapshot ->
                    documentSnapshot.toObject(Message::class.java)?.let { message ->
                        messagesList.add(message)
                    }
                }
                trySend(Resource.Success(messagesList.sortedBy { message -> message.date }
                    .toList()))
            }

        awaitClose { listener.remove() }
    }.flatMapMerge { resource ->
        resource.data?.forEach { message ->
            userRepo.getUser(message.userId).collect { user ->
                message.sender = (user as Resource.Success).data
            }
        }
        flow { emit(resource) }
    }

    private companion object {
        const val TAG = "MessageRepo"
    }
}