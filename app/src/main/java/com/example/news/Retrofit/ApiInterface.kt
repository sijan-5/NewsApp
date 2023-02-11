package com.example.news.Retrofit


import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


//const val BASE_URL = "https://newsapi.org/"
const val apiKey="cd2baf0bf41f42b1a28475a96e162421"
const val BASE_URL = "https://newsapi.org/"
interface ApiInterface {

    @GET("v2/top-headlines?apiKey=$apiKey")
    suspend fun getNews(@Query("country") country:String,@Query("page") page : Int) : Response<News>



    @GET("v2/everything?apiKey=$apiKey")
    suspend fun searchNews(@Query("q") q:String,@Query("page") page : Int) : Response<News>

    object MyObject
    {
        private var interfaceObject : ApiInterface?= null

        fun getInstance(): ApiInterface
        {
            if (interfaceObject == null)
            {
                val retrofitObject = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                interfaceObject = retrofitObject.create(ApiInterface::class.java)


            }
            return  interfaceObject!!

        }

    }



}