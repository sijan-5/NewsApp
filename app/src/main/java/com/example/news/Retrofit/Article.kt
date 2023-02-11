package com.example.news.Retrofit

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "MyTable", indices = [Index(value = ["author"], unique = true)] )

data class Article(

    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,

    @PrimaryKey(autoGenerate = true)
    val id:Int?=0
) : Serializable {

    override fun hashCode(): Int {
        var result = id.hashCode()
        if (url.isNullOrEmpty()) {
            result = 31 * result + url.hashCode()
        }
        return result
    }
}



