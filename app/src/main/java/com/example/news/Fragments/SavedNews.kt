package com.example.news.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Half.toFloat
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.Adapter.MyListAdapter
import com.example.news.MVVM.MainViewModel
import com.example.news.MVVM.MainViewModelFactory
import com.example.news.MVVM.Repository
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.Retrofit.ApiInterface
import com.example.news.Retrofit.Article
import com.example.news.Retrofit.LikedArticle
import com.example.news.db.MyDataBase
import com.example.news.db.MyDataBase.MyObjectDB.databaseObject
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.android.synthetic.main.fragment_saved_news.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Array.get
import kotlin.math.roundToInt


class SavedNews : Fragment() {

    lateinit var mymainViewModel: MainViewModel
    lateinit var databaseObject: MyDataBase
    lateinit var myAdapter: MyListAdapter
    lateinit var convertedArticleList : ArrayList<Article>




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_saved_news, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myInterfaceObject = ApiInterface.MyObject.getInstance()
        databaseObject = MyDataBase.MyObjectDB.getDBInstance(activity as MainActivity)

        val myRepository = Repository(myInterfaceObject, databaseObject)

        mymainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(myRepository)
        ).get(MainViewModel::class.java)



        myAdapter = MyListAdapter()

        savedNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = myAdapter
        }




        // show saved news in savednews fragment
        lifecycleScope.launch(Dispatchers.Main) {

            //abstract data from saved room database and converting likedartcile datacass object to
            // Artticle data class article

            convertedArticleList = ArrayList()

            mymainViewModel.abstractSavedNews().observe(viewLifecycleOwner, Observer {
                it.forEach { eachLikedArticle ->

                    val obj = toArticle(eachLikedArticle)
                    convertedArticleList.add(obj)
                }

                myAdapter.submitList(convertedArticleList)

            })


            //clicking the item of save news
            myAdapter.setOnItemClickListener {

                val bundle = Bundle().apply {

                    putSerializable("article", it)
                }
                convertedArticleList.clear()
                findNavController().navigate(R.id.action_savedNews_to_article, bundle)
            }

        }





        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return true
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.

                val deletedCourse: Article = myAdapter.currentList[viewHolder.adapterPosition]

                mymainViewModel.deleteSavedNews(deletedCourse.id!!)



            }

        }).attachToRecyclerView(savedNewsRecyclerView)


    }





   private fun toArticle(rawObject: LikedArticle) = Article(
        rawObject.author, rawObject.content,
        rawObject.description, rawObject.publishedAt, rawObject.source, rawObject.title,
        rawObject.url, rawObject.urlToImage, rawObject.id
    )
}