package com.example.news.db

import androidx.room.TypeConverter
import com.example.news.Retrofit.Source

class Convertors {

    @TypeConverter
    fun toString(value : Source) :String
    {
        return value.name
    }

    @TypeConverter
    fun toSource(name : String) : Source
    {
        return  name.let { Source(it,it) }
    }
}