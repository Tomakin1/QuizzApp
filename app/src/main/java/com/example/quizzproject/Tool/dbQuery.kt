package com.example.quizzproject.Tool

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DbQuery {

    private val db = Firebase.firestore

    fun createUserData(email: String, name: String) {
        val userData = hashMapOf(
            "email_id" to email,
            "name" to name,
            "total_score" to 0,
            "level" to 1,
            "correctCount" to 0,
            "wrongCount" to 0  // Yeni kullanıcı oluştururken eklediğiniz alan
        )

        val userDoc: DocumentReference =
            db.collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)

        val batch = db.batch()
        batch.set(userDoc, userData)

        val countDoc: DocumentReference = db.collection("Users").document("Total_users")
        batch.update(countDoc, "count", FieldValue.increment(1))

        batch.commit()
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    fun updateTotalScore(score: Int) {
        val userDoc: DocumentReference? = FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
            db.collection("Users").document(uid)
        }

        userDoc?.let { doc ->
            doc.update("total_score", FieldValue.increment(score.toLong()))
                .addOnSuccessListener {

                }
                .addOnFailureListener { e ->

                }
        }
    }
    fun updateCorrectCount() {
        FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
            db.collection("Users").document(uid).update("correctCount", FieldValue.increment(1))
                .addOnSuccessListener {
                    Log.d(TAG, "Correct count updated successfully")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error updating correct count", e)
                }
        }
    }

    fun updateWrongCount() {
        FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
            db.collection("Users").document(uid).update("wrongCount", FieldValue.increment(1))
                .addOnSuccessListener {
                    Log.d(TAG, "Wrong count updated successfully")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error updating wrong count", e)
                }
        }
    }


    fun fetchAndSaveScoreToSharedPreferences(context: Context) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = Firebase.firestore
            val userDoc = db.collection("Users").document(user.uid)

            userDoc.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val totalScore = document.getLong("total_score") ?: 0

                        val sharedPreferences =
                            context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putInt("total_score", totalScore.toInt()).apply()
                    } else {
                        Log.d(TAG, "Belirtilen belge bulunamadı")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get işlemi başarısız oldu", exception)
                }
        }
    }

    fun getUserName(callback: (String?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = Firebase.firestore
            val userDoc = db.collection("Users").document(user.uid)

            userDoc.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userName = document.getString("name")
                        callback(userName)
                    } else {
                        Log.d(TAG, "Belirtilen belge bulunamadı")
                        callback(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get işlemi başarısız oldu", exception)
                    callback(null)
                }
        } ?: callback(null)
    }
}
