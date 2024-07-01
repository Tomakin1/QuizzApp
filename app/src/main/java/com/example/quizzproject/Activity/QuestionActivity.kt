package com.example.quizzproject.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzproject.Adapter.QuestionAdapter
import com.example.quizzproject.Domain.QuestionModel
import com.example.quizzproject.Tool.DbQuery
import com.example.quizzproject.databinding.ActivityQuestionBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.Timer
import kotlin.concurrent.timer

class QuestionActivity : AppCompatActivity(), QuestionAdapter.score {

    private lateinit var binding : ActivityQuestionBinding
    var position:Int=0
    var receivedList:MutableList<QuestionModel> = mutableListOf()
    var allscore=0
    var remainingTime=0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        startCountdown()



        receivedList = intent.getParcelableArrayListExtra<QuestionModel>("list")?.toMutableList() ?: mutableListOf()
        receivedList.shuffle()




        binding.apply {
            backBtn.setOnClickListener { finish()  }
            progressBar.progress=1

            loadQuestionAtPosition(position)
        }









        binding.apply {

            timeOption.setOnClickListener {
                var isExtraTimeUsed = false

                binding.apply {
                    timeOption.setOnClickListener {
                        if (!isExtraTimeUsed) {
                            cancelCountdown()
                            startCountdown()
                            timeOption.visibility=View.INVISIBLE

                            isExtraTimeUsed = true
                        } else {


                        }
                    }
                }
            }

            var ipucuUsed = false

            ipucu.setOnClickListener {

                if (!ipucuUsed) {
                    val que = receivedList[position].question
                    val clue = receivedList[position].clue
                    ipucu.visibility=View.INVISIBLE



                    binding.clueTxt.text = clue.toString()
                    clueTxt.visibility = View.VISIBLE



                    ipucuUsed = true
                }
            }




        }














        binding.apply {
            backBtn.setOnClickListener { finish() }
            progressBar.progress=1

            questionTxt.text= receivedList[position].question





            loadAnswers()

            rightArrow.setOnClickListener {
                cancelCountdown()
                startCountdown()
                binding.clueTxt.visibility=View.INVISIBLE


                if (progressBar.progress==10){

                    endGame()


                    return@setOnClickListener

                }

                position++
                progressBar.progress=progressBar.progress +1
                questionNumberTxt.text="Soru" +progressBar.progress+"/10"
                questionTxt.text=receivedList[position].question





                loadAnswers()
            }




        }
        binding.backBtn.setOnClickListener{
            val intent = Intent(this@QuestionActivity,LevelActivity::class.java)
            startActivity(intent)
            cancelCountdown()
        }

    }

    fun loadAnswers() {
        val users: MutableList<String> = mutableListOf()

        users.add(receivedList[position].answer_1.toString())
        users.add(receivedList[position].answer_2.toString())
        users.add(receivedList[position].answer_3.toString())
        users.add(receivedList[position].answer_4.toString())

        if (receivedList[position].clickedAnswer!=null) users.add(receivedList[position].clickedAnswer.toString())

        val questionAdapter by lazy {
            QuestionAdapter(receivedList[position].correctAnswer.toString(),users,this)

        }





        questionAdapter.differ.submitList(users)
        binding.questionList.apply {
            layoutManager = LinearLayoutManager(this@QuestionActivity)
            adapter = questionAdapter
        }








    }
    private fun loadQuestionAtPosition(position: Int) {
        binding.apply {
            questionTxt.text = receivedList[position].question



            loadAnswers()
        }
    }

    private fun addTotalScoreToDatabase(score: Int) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            DbQuery().updateTotalScore(score)
        }
    }
    private fun endGame() {

        val intent = Intent(this@QuestionActivity, ScoreActivity::class.java)
        intent.putExtra("Score", allscore)
        addTotalScoreToDatabase(allscore)
        startActivity(intent)
        finish()
        cancelCountdown()



    }




    override fun amount(number: Int, clickedAnswer: String) {
        allscore += number
        receivedList[position].clickedAnswer = clickedAnswer
    }

    private var countdownTimer: Timer? = null

    private fun startCountdown() {

        val totalTime = 30
        var remainingTime = totalTime

        countdownTimer = timer(period = 1000) {
            runOnUiThread {
                if (remainingTime > 0) {
                    binding.timeTxt.text = remainingTime.toString()
                    remainingTime--

                } else {
                    binding.timeTxt.text = "0"
                    cancelCountdown()
                    val intent=Intent(this@QuestionActivity,ScoreActivity::class.java)
                    intent.putExtra("Score",allscore)
                    startActivity(intent)
                    finish()


                }
            }
        }
    }


    private fun cancelCountdown() {
        countdownTimer?.cancel()
    }






}