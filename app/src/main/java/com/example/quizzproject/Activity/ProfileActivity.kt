package com.example.quizzproject.Activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzproject.R
import com.example.quizzproject.Tool.DbQuery
import com.example.quizzproject.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private val dbQuery = DbQuery()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        auth = Firebase.auth

        binding.apply {
            bottomMenu.setItemSelected(R.id.Profile)
            bottomMenu.setOnItemSelectedListener {
                when (it) {
                    R.id.home -> startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                    R.id.Board -> startActivity(Intent(this@ProfileActivity, LeaderActivity::class.java))
                }
            }

            backBtn.setOnClickListener {
                val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                startActivity(intent)
            }

            exitTxt.setOnClickListener {
                showLogoutConfirmationDialog()
            }
        }


        loadUserProfile()
    }

    private fun loadUserProfile() {

        dbQuery.getUserName { userName ->
            if (userName != null) {
                binding.dbName.text = userName
            } else {
                binding.dbName.text = "İsim Bulunamadı"
            }
        }


        dbQuery.fetchAndSaveScoreToSharedPreferences(this)


        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userScore = sharedPreferences.getInt("total_score", 0)
        binding.dbScore.text = userScore.toString()
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Çıkış Yap")
        builder.setMessage("Çıkış yapmak istediğinizden emin misiniz?")
        builder.setPositiveButton("Evet") { dialogInterface: DialogInterface, i: Int ->
            auth.signOut()
            val intent = Intent(this@ProfileActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton("Hayır", null)
        builder.show()
    }
}