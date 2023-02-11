package com.example.news.Fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreakingNews : Fragment(R.layout.fragment_breaking_news) {

    lateinit var mymainViewModel: MainViewModel
    lateinit  var databaseObject : MyDataBase

    lateinit var myAdapter: MyListAdapter




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //listAdapter things


        myAdapter = MyListAdapter()

        breakingNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = myAdapter
        }


        myAdapter.setOnItemClickListener {
            if (checkInternet(requireContext())) {
                val bundle = Bundle()
                bundle.putSerializable("article", it)

                findNavController().navigate(R.id.action_breakingNews_to_article, bundle)
            }
        }




        //viewModel things
        val myInterfaceObject = ApiInterface.MyObject.getInstance()
        databaseObject = MyDataBase.MyObjectDB.getDBInstance(activity as MainActivity)

        val myRepository = Repository(myInterfaceObject, databaseObject)

        mymainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(myRepository)
        ).get(MainViewModel::class.java)




        //get data if internet is online
        if (checkInternet(requireContext()))
        {

                // listAdapter component
                    hideProgress()

                    mymainViewModel.getNews("us")
                    mymainViewModel.articleList.observe(viewLifecycleOwner, Observer {
                    myAdapter.submitList(it!!)


                })
            }
        else
        {
            // if internet is offline
            lifecycleScope.launch(Dispatchers.Main) {

               mymainViewModel.abstractData().observe(viewLifecycleOwner, Observer {
                   myAdapter.submitList(it)
               })

                hideProgress()

            }
        }

    }


    // checking if the internet available
    fun checkInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when
            {
                activeNetwork!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->true

                else -> false
            }


        }
        else
        {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }

    }

    fun hideProgress()
    {

        // checking progressbar status

        mymainViewModel.pStatus.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = View.INVISIBLE
        })
    }




}