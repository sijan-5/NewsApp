package com.example.news.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.news.Retrofit.Article
import com.example.news.Retrofit.LikedArticle
import retrofit2.http.DELETE


@Dao
interface MyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(article: List<Article>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(likeArticle : LikedArticle)


    @Query("Delete from MyTable")
    fun delete()

    @Update()
    suspend fun update(article: Article)

    @Query("Select *from MyTable limit 30")
    fun abstractData() : LiveData<List<Article>>

    @Query("delete from likeArticleTable where id=:deletingId")
    suspend fun deleteSavedNews(deletingId :Int)

    @Query("select *from likeArticleTable")
     fun abstractSavedNews() : LiveData<List<LikedArticle>>
}