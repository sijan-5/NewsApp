package com.example.news.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.Adapter.MyListAdapter
import com.example.news.MVVM.MainViewModel
import com.example.news.MVVM.MainViewModelFactory
import com.example.news.MVVM.Repository
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.Retrofit.ApiInterface
import com.example.news.db.MyDataBase
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.*


class SearchNews : Fragment() {

    private lateinit var databaseObject : MyDataBase
    private lateinit var mymainViewModel : MainViewModel
    private lateinit var myAdapter : MyListAdapter
    private lateinit var job:Job
    private lateinit var breakingNews: BreakingNews


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search_news,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        breakingNews = BreakingNews()

        //listAdapter things
        myAdapter = MyListAdapter()
        searchRecyclerView.layoutManager = LinearLayoutManager(activity)
        searchRecyclerView.adapter = myAdapter


        myAdapter.setOnItemClickListener {


            val bundle = Bundle().apply {

                putSerializable("article",it)
            }

          findNavController().navigate(R.id.action_searchNews_to_article,bundle)
        }

        //viewModel things
        val myInterfaceObject = ApiInterface.MyObject.getInstance()
        databaseObject = MyDataBase.MyObjectDB.getDBInstance(activity as MainActivity)

        val myRepository = Repository(myInterfaceObject, databaseObject)

        mymainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(myRepository)
        ).get(MainViewModel::class.java)



            searchItem.addTextChangedListener { editable ->

                if (breakingNews.checkInternet(requireContext())) {

                CoroutineScope(Dispatchers.Main).launch {

                    job = CoroutineScope(Dispatchers.Default).launch {
                        makeDelay()
                    }
                    job.join()

                    if (editable.toString().isNotEmpty() && view!=null ) {


                        mymainViewModel.searchNews(editable.toString())
                        if(isAdded) {


                            mymainViewModel.searchList.observe(
                                requireParentFragment().viewLifecycleOwner,
                                Observer {

                                    myAdapter.submitList(it!!)

                                })
                        }

                    }

                }
            }



        }


    }

    private suspend fun  makeDelay() {
       delay(2000)
    }





}