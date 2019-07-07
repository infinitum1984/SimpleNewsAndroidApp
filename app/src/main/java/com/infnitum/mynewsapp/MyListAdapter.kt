package com.infnitum.mynewsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import java.util.ArrayList

class MyListAdapter : BaseAdapter() {

    lateinit var my_list:ArrayList<NewsModel>
    lateinit var context: Context
    lateinit var layoutInflater:LayoutInflater

    fun MyListAdapter( c:Context, list:ArrayList<NewsModel>){
        my_list=list
        context=c
        layoutInflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return my_list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return my_list.get(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View
        if (convertView==null){
                view=layoutInflater.inflate(R.layout.list_item,null)
        }else{
            view=convertView
        }



        return view
    }

}