package com.example.quizzproject.Domain


data class UserModel(
    val id: String, // Firebase'den gelen belge kimliği (UID)
    val name: String,
    val email_id: String,
    val total_score: Int // Toplam skor
)