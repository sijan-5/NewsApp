package com.example.news.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.news.R
import com.example.news.Retrofit.Article
import kotlinx.android.synthetic.main.recycler_item.view.*



class MyListAdapter :ListAdapter<Article,MyListAdapter.MyViewHolder>(MyDiffUtil()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = getItem(position)
        holder.onBind(item)


        // to click each item of list
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {it(item) }

        }
    }




    class MyViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView)
    {
        fun onBind(article: Article)  = with(itemView)
        {

            Glide.with(this)
                .load(article.urlToImage)
                .error(R.drawable.notfound)
                .into(imageView)

            webSource.text = article.source?.name
            newsDate.text = article.publishedAt
            newsTitle.text = article.title
            newsDescription.text = article.description

        }

    }

    // lamda function for handling web view
    private var onItemClickListener : ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener : (Article) ->Unit)
    {
        onItemClickListener = listener
    }



    // implementing diffutil class
    class MyDiffUtil : DiffUtil.ItemCallback<Article>()
    {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return  oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }



}


