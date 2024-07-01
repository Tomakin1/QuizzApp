package com.example.quizzproject.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzproject.Tool.DbQuery
import com.example.quizzproject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth:FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        // Initialize Firebase Auth
        auth = Firebase.auth

        val currentUser = auth.currentUser

        if (currentUser!=null){
            val intent = Intent(this@RegisterActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }









        binding.apply {


            registerBtn.setOnClickListener {
                val email = email.text.toString()
                val pass = password.text.toString()
                val name = binding.username.text.toString()

                if (email.equals("")|| pass.equals("")){
                    Toast.makeText(this@RegisterActivity,"E-mail Ve Şifre Giriniz!!",Toast.LENGTH_LONG).show()

                }else{
                    auth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener {

                        DbQuery().createUserData(email, name)
                        intent = Intent(this@RegisterActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }.addOnFailureListener {
                        Toast.makeText(this@RegisterActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("EMAIL", email)
                editor.putString("NAME", name)
                editor.apply()



            }

            entryBtn.setOnClickListener {

                val email = email.text.toString()
                val pass = password.text.toString()

                if (email.equals("")|| pass.equals("")){
                    Toast.makeText(this@RegisterActivity,"E-mail Ve Şifre Giriniz!!",Toast.LENGTH_LONG).show()

                }else{
                    auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener {
                        intent = Intent(this@RegisterActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }.addOnFailureListener {
                        Toast.makeText(this@RegisterActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }

                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("EMAIL", email)
                editor.apply()










            }
        }






    }



}