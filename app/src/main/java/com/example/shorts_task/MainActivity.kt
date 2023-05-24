package com.example.shorts_task

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shorts_task.adapter.Adapter
import com.example.shorts_task.api.ApiInterface
import com.example.shorts_task.api.ApiUtilities
import com.example.shorts_task.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private lateinit var rv : RecyclerView;
    private var currentpage = 0
    private lateinit var list: MutableList<Post>


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv = findViewById(R.id.rv)
        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)

         list = mutableListOf<Post>()


        var adapter = Adapter(list, this)
        rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL )
        rv.adapter = adapter

        runBlocking {
            launch(Dispatchers.IO ) {
                Log.d("Api data", "Calling the API")

                val result = apiInterface.getData(currentpage)
                list = result.data.posts as MutableList<Post>


                Log.d("Api data " , result.toString())
                adapter.UpdateList(list)
            }
        }

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
                val lastVisibleItemPosition = getLastVisibleItemPosition(lastVisibleItemPositions)

                // Check if the last visible item has been reached
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    // Trigger your method here

                    runBlocking {
                        launch(Dispatchers.IO ) {
                            val result = apiInterface.getData(currentpage+1)
                            list.addAll(result.data.posts)


                            Log.d("Api data " , result.toString())

                        }
                        adapter.UpdateList(list)
                    }
                    currentpage++;
                }


            }
        })

    }

    fun getLastVisibleItemPosition(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0 || lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
    
}

