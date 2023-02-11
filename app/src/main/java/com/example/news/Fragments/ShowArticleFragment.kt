package com.example.news.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.news.MVVM.MainViewModel
import com.example.news.MVVM.MainViewModelFactory
import com.example.news.MVVM.Repository
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.Retrofit.ApiInterface
import com.example.news.Retrofit.Article
import com.example.news.Retrofit.LikedArticle
import com.example.news.db.MyDataBase
import kotlinx.android.synthetic.main.fragment_show_article2.*


class ShowArticleFragment : Fragment() {

    lateinit var mymainViewModel: MainViewModel
    lateinit  var databaseObject : MyDataBase


    val args  : ShowArticleFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_article2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.article

        webView1?.apply {
            webViewClient = WebViewClient()
            webView1.loadUrl(article.url.toString())
        }

        //viewModel things
        val myInterfaceObject = ApiInterface.MyObject.getInstance()
        databaseObject = MyDataBase.MyObjectDB.getDBInstance(activity as MainActivity)

        val myRepository = Repository(myInterfaceObject, databaseObject)

        mymainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(myRepository)
        ).get(MainViewModel::class.java)

        //clicking the love button
        saveFloatingButton.setOnClickListener {

            val convertedObject = objectConverting(article)
            mymainViewModel.insertOne(convertedObject)

            Toast.makeText(context,"This article has been saved",Toast.LENGTH_SHORT).show()
        }
    }



    fun objectConverting(fromArticle : Article) = LikedArticle(fromArticle.author,
    fromArticle.content, fromArticle.description, fromArticle.publishedAt, fromArticle.source,
    fromArticle.title,fromArticle.url, fromArticle.urlToImage, fromArticle.id)

}