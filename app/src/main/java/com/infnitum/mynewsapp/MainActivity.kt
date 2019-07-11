package com.infnitum.mynewsapp


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.ArrayList
import android.content.Intent
import android.net.Uri
import android.icu.util.ULocale.getCountry
import android.content.Context.TELEPHONY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager
import android.widget.Toast
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager








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

        var locale = applicationContext.getResources().getConfiguration().locale.getCountry()
        locale=locale.toLowerCase()



        val ConnectionManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected == true) {
            get_news("https://newsapi.org/v2/top-headlines?" +
                    "country=$locale&" +
                    "apiKey=e298a073744747bbb1b4e7113f395f2b")

            listNews.setOnItemClickListener { parent, view, position, id ->
                show_webview(arrayList_details[position].url)

            }
        } else {
            Toast.makeText(this@MainActivity, "Network Not Available", Toast.LENGTH_LONG).show()

        }


    }

    fun show_webview(url: String){
        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(false)
        dialog .setContentView(R.layout.webview_dialog)

        val webView = dialog .findViewById(R.id.my_webview)as WebView
        webView.webViewClient= WebViewClient()
        webView.webChromeClient= WebChromeClient()
        webView.settings.javaScriptEnabled=true
        webView.settings.builtInZoomControls=true
        webView.loadUrl(url)

        val close_btn=dialog.findViewById<ImageButton>(R.id.close_dialog)
        close_btn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<ImageButton>(R.id.browser).setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webView.url))
            startActivity(browserIntent)
        }
        dialog.findViewById<ImageButton>(R.id.left_webview).setOnClickListener {
            if (webView.canGoBack()){
                webView.goBack()
            }
        }
        dialog.findViewById<ImageButton>(R.id.right_webview).setOnClickListener {
            if(webView.canGoForward()){
                webView.goForward()
            }
        }
        dialog .show()


    }

    fun get_news(url: String) {
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

                    listNews.adapter=MyListAdapter(applicationContext,arrayList_details)
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e(TAG,e.message)
            }
        })
    }
}
