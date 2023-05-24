package com.example.shorts_task

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.exo_demo.ExoPlayerItem
import com.example.shorts_task.adapter.VideoAdapter
import com.example.shorts_task.model.APIdata
import com.example.shorts_task.model.Comment
import com.example.shorts_task.model.Creator
import com.example.shorts_task.model.CustomData
import com.example.shorts_task.model.Post
import com.example.shorts_task.model.Reaction
import com.example.shorts_task.model.Submission

class VideoActivity : AppCompatActivity() {
    private lateinit var posts : ArrayList<Post>
    private lateinit var adapter : VideoAdapter
    private lateinit var exoPlayerItems : ArrayList<ExoPlayerItem>
    private lateinit var viewPager2 : ViewPager2

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video);

        window.setNavigationBarColor(Color.BLACK);
        window.statusBarColor = Color.BLACK


        viewPager2 = findViewById<ViewPager2>(R.id.viewpager2)


        posts = intent.getSerializableExtra("POSTS") as ArrayList<Post>


        exoPlayerItems = ArrayList()

        adapter = VideoAdapter(this, posts, object : VideoAdapter.OnVideosPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }

        })
        viewPager2.adapter = adapter
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val previousIndex = exoPlayerItems.indexOfFirst { it.exoplayer.isPlaying }
                if(previousIndex!=-1)
                {
                    val player = exoPlayerItems[previousIndex].exoplayer
                    player.pause()
                    player.playWhenReady = false
                }

                val newIndex = exoPlayerItems.indexOfFirst{it.position == position}
                if(newIndex!=-1)
                {
                    val player = exoPlayerItems[newIndex].exoplayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }


    override fun onPause() {
        super.onPause()
        val index = exoPlayerItems.indexOfFirst {  it.exoplayer.isPlaying}

        if(index!=-1)
        {
            val player = exoPlayerItems.get(index).exoplayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
        val index = exoPlayerItems.indexOfLast {  it.position == viewPager2.currentItem}

        if(index!=-1)
        {
            val player = exoPlayerItems.get(index).exoplayer
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(exoPlayerItems.isNotEmpty())
        {
            for(item in exoPlayerItems)
            {
                val player = item.exoplayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }
}