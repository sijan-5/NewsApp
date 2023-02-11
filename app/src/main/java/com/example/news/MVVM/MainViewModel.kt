package com.example.news.MVVM


import androidx.lifecycle.*
import com.example.news.Retrofit.Article
import com.example.news.Retrofit.LikedArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(private val repository: Repository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val articleList = MutableLiveData<List<Article>>()

    val searchList = MutableLiveData<List<Article>>()

    var  pStatus = MutableLiveData<Boolean>()

    val savedNewsList = MutableLiveData<List<LikedArticle>>()






    // getting data from network using retrofit
    fun getNews(country : String)
    {
        val page = 1

        viewModelScope.launch(Dispatchers.Default) {

            val response = repository.getNews(country,page)

            if(response.isSuccessful && response.body()!=null)
            {
                articleList.postValue(response.body()?.articles)
                repository.insert(response.body()!!.articles)


                pStatus.postValue(false)
                //repository.delete()
            }

            else
            {
                onError("Error : ${response.message()}")
            }
        }
    }



    // search news for searchnews fragment
    fun searchNews(topic:String)
    {
        viewModelScope.launch {

            val searchResponse = repository.searchNews(topic,1)

            if (searchResponse.isSuccessful && searchResponse!=null)
            {

                searchList.postValue(searchResponse.body()?.articles)
                pStatus.postValue(false)
            }


        }
    }


    // function if any error arrives while fetching data from internet
     fun onError(s: String) {

        errorMessage.postValue(s)
    }

    // abstract data from database if internet is not available to show in breaking news fragment
    fun abstractData() : LiveData<List<Article>>
    {
       return  repository.getData()
    }


    // to add saved news into database
    fun insertOne(fav : LikedArticle)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertOne(fav)
        }

    }

    // abstract savednews to show in savednews fragment
    fun abstractSavedNews() : LiveData<List<LikedArticle>>
    {
        return repository.abstractSavedNews()
    }


    fun deleteSavedNews(deletingId : Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSavedNews(deletingId)
        }
    }



    fun deleteMyTable()
    {
        repository.deleteMyTable()
    }


}