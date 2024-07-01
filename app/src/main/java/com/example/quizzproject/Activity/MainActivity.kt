package com.example.quizzproject.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzproject.R
import com.example.quizzproject.Tool.DbQuery
import com.example.quizzproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val dbQuery = DbQuery()

     private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        DbQuery().fetchAndSaveScoreToSharedPreferences(this)

        dbQuery.getUserName { userName ->
            if (userName != null) {
                binding.dbNick.text = userName
            } else {
                binding.dbNick.text = "İsim Bulunamadı"
            }
        }



        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("NAME", "")
        val totalScore = sharedPreferences.getInt("total_score", 0)













        binding.apply {
            dbScore.text=totalScore.toString()



            dbNick.text="$name"

            singleBtn.setOnClickListener {
                val intent = Intent(this@MainActivity,LevelActivity::class.java)
                startActivity(intent)



            }


            bottomMenu.setItemSelected(R.id.home)
            bottomMenu.setOnItemSelectedListener {
                if (it== R.id.Profile){
                    startActivity(Intent(this@MainActivity,ProfileActivity::class.java))


                }else if(it== R.id.Board){
                    startActivity(Intent(this@MainActivity,LeaderActivity::class.java))
                }
            }





        }

    }









}