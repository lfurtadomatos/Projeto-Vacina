package com.br.projetodevacina.db

import com.br.projetodevacina.data.User
import com.br.projetodevacina.data.VaccineRecord
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FBDatabase {
    private val db = FirebaseFirestore.getInstance()

    fun saveUserProfile(user: User, onComplete: (Boolean) -> Unit) {
        db.collection("users")
            .document(user.uid)
            .set(user)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun addVaccineRecord(uid: String, record: VaccineRecord, onComplete: (Boolean) -> Unit) {
        val docRef = db.collection("users").document(uid).collection("vaccines").document()
        val finalRecord = record.copy(id = docRef.id)

        docRef.set(finalRecord)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getVaccineRecords(uid: String): Flow<List<VaccineRecord>> = callbackFlow {
        val listener = db.collection("users")
            .document(uid)
            .collection("vaccines")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val records = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(VaccineRecord::class.java)
                } ?: emptyList()

                trySend(records)
            }

        awaitClose { listener.remove() }
    }
}