package com.infnitum.mynewsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.util.ArrayList

class MyListAdapter(c:Context, list:ArrayList<NewsModel>) : BaseAdapter() {

    var my_list:ArrayList<NewsModel>
    var context: Context
    var layoutInflater:LayoutInflater

    init{
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

        val title = view.findViewById<TextView>(R.id.title)
        val content = view.findViewById<TextView>(R.id.content)
        val image = view.findViewById<ImageView>(R.id.img)

        title.setText(my_list[position].title)

        if (!my_list[position].author.equals("null")) {
            content.setText(my_list[position].author)
        }

        //eas set img from url
        Picasso.get()
            .load(my_list[position].urlToImage)
            .resize(60, 60)
            .centerCrop()
            .into(image)



        return view
    }

}