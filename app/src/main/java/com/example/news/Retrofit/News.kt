package com.example.news.Retrofit

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)