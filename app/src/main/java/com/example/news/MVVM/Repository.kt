package com.example.news.MVVM

import androidx.lifecycle.LiveData
import com.example.news.Retrofit.ApiInterface
import com.example.news.Retrofit.Article
import com.example.news.Retrofit.LikedArticle
import com.example.news.db.MyDataBase

class Repository(private val retrofitObject : ApiInterface,
private val databaseObj : MyDataBase) {

    suspend fun getNews(q: String,page:Int) = retrofitObject.getNews(q,page)


    fun getData()  :LiveData<List<Article>> = databaseObj.getDaoObject().abstractData()


    suspend fun insert(article :List<Article>) = databaseObj.getDaoObject().insertAll(article)

    fun deleteMyTable() = databaseObj.getDaoObject().delete()


    suspend fun searchNews(topic:String, page: Int) = retrofitObject.searchNews(topic,page)



    suspend fun insertOne(likeArticle : LikedArticle) = databaseObj.getDaoObject().insertOne(likeArticle)

    fun abstractSavedNews() : LiveData<List<LikedArticle>> = databaseObj.getDaoObject().abstractSavedNews()

    suspend fun deleteSavedNews(deletingId : Int) = databaseObj.getDaoObject().deleteSavedNews(deletingId)
}