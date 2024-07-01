
package com.example.quizzproject.Activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzproject.Adapter.LeaderAdapter
import com.example.quizzproject.Domain.UserModel
import com.example.quizzproject.R
import com.example.quizzproject.databinding.ActivityLeaderBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LeaderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeaderBinding
    private val leaderAdapter by lazy { LeaderAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("EMAIL", "")

        loadData { users ->
            binding.apply {
                if (users.size >= 3) {
                    scoreTxt1.text = users[0].total_score.toString()
                    scoreTxt2.text = users[1].total_score.toString()
                    scoreTxt3.text = users[2].total_score.toString()
                    titleTop1Txt.text = users[0].name
                    titleTop2Txt.text = users[1].name
                    titleTop3Txt.text = users[2].name

                    val list = users.subList(3, users.size)
                    leaderAdapter.differ.submitList(list)
                }
                bottomMenu.setItemSelected(R.id.Board)
                bottomMenu.setOnItemSelectedListener {
                    if (it == R.id.home) {
                        startActivity(Intent(this@LeaderActivity, MainActivity::class.java))
                    } else if (it == R.id.Profile) {
                        startActivity(Intent(this@LeaderActivity, ProfileActivity::class.java))
                    }
                }
                leaderView.layoutManager = LinearLayoutManager(this@LeaderActivity)
                leaderView.adapter = leaderAdapter
            }
        }
    }

    private fun loadData(callback: (List<UserModel>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .orderBy("total_score", Query.Direction.DESCENDING) // "total_score" olarak güncellendi
            .get()
            .addOnSuccessListener { result ->
                val users = mutableListOf<UserModel>()
                for (document in result) {
                    val id = document.id
                    val name = document.getString("name") ?: ""
                    val email = document.getString("email_id") ?: ""
                    val totalScore = document.getLong("total_score")?.toInt() ?: 0
                    val user = UserModel(id, name, email, totalScore)
                    users.add(user)
                }
                callback(users)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                callback(emptyList())
            }
    }
}

