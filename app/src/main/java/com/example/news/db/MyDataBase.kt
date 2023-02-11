package com.example.news.db

import android.content.Context
import android.widget.Toast
import androidx.room.*
import com.example.news.Retrofit.Article
import com.example.news.Retrofit.LikedArticle
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import retrofit2.Converter


@Database(entities = [Article::class, LikedArticle::class], version = 1)

@TypeConverters(Convertors::class)
abstract class MyDataBase : RoomDatabase() {

    abstract fun getDaoObject() : MyDao


    object MyObjectDB
    {
        @Volatile
        var databaseObject : MyDataBase? = null

         fun getDBInstance(context:Context) : MyDataBase
        {

            if (databaseObject == null)
            {

                databaseObject = Room.databaseBuilder(context,MyDataBase::class.java,"Sijan.DB").allowMainThreadQueries().build()
            }

            return databaseObject!!
        }

    }
}