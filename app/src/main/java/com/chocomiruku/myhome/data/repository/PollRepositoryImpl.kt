package com.chocomiruku.myhome.data.repository

import android.util.Log
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.Poll
import com.chocomiruku.myhome.domain.repository.PollRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PollRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : PollRepository {

    private val pollsCollectionRef: CollectionReference
        get() = firestore.collection("polls")

    override suspend fun addPoll(poll: Poll) {
        try {
            pollsCollectionRef.add(poll)
        } catch (e: Exception) {
            Log.d(TAG, "addingPoll:failure", e)
        }
    }

    override suspend fun closePoll(pollId: String) {
        val pollFieldMap = mutableMapOf<String, Any>()
        pollFieldMap["closed"] = true
        try {
            val pollDocRef = firestore.document("polls/${pollId}")
            pollDocRef.update(pollFieldMap).await()
            Log.d(TAG, "updatingPoll:success")
        } catch (e: Exception) {
            Log.d(TAG, "updatingPoll:failure", e)
        }
    }

    override suspend fun vote(pollId: String, selectedOptions: List<String>) {
        val pollFieldMap = mutableMapOf<String, Any>()
        try {
            val pollDocRef = firestore.document("polls/${pollId}")
            val poll = pollDocRef.get().await().toObject(Poll::class.java)
            poll?.let {
                val options = poll.options
                selectedOptions.forEach { option ->
                    options[option] = options.getValue(option).plus(1)
                }
                pollFieldMap["options"] = options

                val voted = poll.voted
                voted.add(auth.currentUser?.uid ?: throw NullPointerException("UID is null."))
                pollFieldMap["voted"] = voted

                pollDocRef.update(pollFieldMap).await()
                Log.d(TAG, "updatingPoll:success")
            }
        } catch (e: Exception) {
            Log.d(TAG, "updatingPoll:failure", e)
        }
    }

    override fun getPolls(): Flow<Resource<List<Poll>>> = callbackFlow {
        val listener = pollsCollectionRef.addSnapshotListener { querySnapshot, firebaseException ->
            if (firebaseException != null) {
                Log.e(TAG, "addingListener:failure", firebaseException)
                return@addSnapshotListener
            }

            val pollsList = mutableListOf<Poll>()
            querySnapshot?.documents?.forEach { documentSnapshot ->
                documentSnapshot.toObject(Poll::class.java)?.let { poll ->
                    poll.hasVoted = poll.voted.contains(
                        auth.currentUser?.uid ?: throw NullPointerException("UID is null.")
                    )
                    pollsList.add(poll)
                }
            }
            trySend(Resource.Success(pollsList.sortedByDescending { poll -> poll.date }.toList()))
        }

        awaitClose { listener.remove() }
    }

    private companion object {
        const val TAG = "PollRepo"
    }
}