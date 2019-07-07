package com.infnitum.mynewsapp


import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var listNews: ListView
    val TAG="myapp"
    //lateinit var listView_details: ListView
    var arrayList_details:ArrayList<NewsModel> = ArrayList()
    //OkHttpClient creates connection pool between client and server
    val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listNews=findViewById(R.id.list_news)

        run("https://newsapi.org/v2/top-headlines?" +
                "country=ua&" +
    "apiKey=e298a073744747bbb1b4e7113f395f2b")


    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: okhttp3.Call, response: Response) {
                var str_response = response.body()!!.string()
                //creating json object
                val json_contact: JSONObject = JSONObject(str_response)
                Log.d(TAG,json_contact.toString())
                //creating json array
                var jsonarray_info: JSONArray = json_contact.getJSONArray("articles")
                var i: Int = 0
                var size: Int = jsonarray_info.length()
                arrayList_details = ArrayList()
                for (i in 0..size - 1) {
                    var json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)
                    var newsModel:NewsModel = NewsModel(json_objectdetail.getJSONObject("source").getString("id"),json_objectdetail.getJSONObject("source").getString("name"),
                        json_objectdetail.getString("author"),json_objectdetail.getString("title"),json_objectdetail.getString("url"),json_objectdetail.getString("urlToImage"),
                        json_objectdetail.getString("publishedAt"),json_objectdetail.getString("content")
                        )
                    arrayList_details.add(newsModel)
                }

                runOnUiThread {
                    //stuff that updates ui
                    Log.d(TAG, arrayList_details.size.toString())

                    for (item in arrayList_details){
                        Log.d(TAG, item.title)
                    }

                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e(TAG,e.message)
            }
        })
    }
}
