package com.example.quizzproject.Tool

import kotlin.concurrent.timer

class CountDownTimer {


    fun main() {
        val totalTime = 30
        var remainingTime = totalTime

        val countdownTimer = timer(period = 1000) {
            if (remainingTime > 0) {
                println("Kalan süre: $remainingTime saniye")
                remainingTime--
            } else {
                println("Süre doldu!")
                cancel()
            }
        }
    }

}