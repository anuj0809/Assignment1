package com.example.shorts_task.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.exo_demo.ExoPlayerItem
import com.example.shorts_task.R
import com.example.shorts_task.model.Post
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource

class VideoAdapter(
    var context : Context,
    var posts : ArrayList<Post>,
    var videosPreparedListener: OnVideosPreparedListener

) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_video, parent, false)
        return VideoViewHolder(itemView, context, videosPreparedListener)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val post = posts.get(position)
        holder.title.setText(post.submission.title)

        holder.setVideoPath(post.submission.mediaUrl)

    }


    inner class VideoViewHolder(private val view: View,
                                val context : Context,
                                var videosPreparedListener: OnVideosPreparedListener
    ) :
        RecyclerView.ViewHolder(view)
    {

        val pbloading = view.findViewById<ProgressBar>(R.id.pd_loading)
        val playerview = view.findViewById<StyledPlayerView>(R.id.exo_player)
        val title = view.findViewById<TextView>(R.id.title_of_video)



        private lateinit var exoPlayer: ExoPlayer
        private lateinit var mediaSource : MediaSource

        fun setVideoPath(url  : String){
            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer.addListener(object : Player.Listener{
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(context, "Can't play this video", Toast.LENGTH_LONG).show()
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)
                    if(playbackState==Player.STATE_BUFFERING)
                    {
                        pbloading.visibility = View.VISIBLE
                    }
                    else if(playbackState == Player.STATE_READY){
                        pbloading.visibility = View.GONE
                    }
                }
            })

            playerview.player = exoPlayer
            exoPlayer.seekTo(0)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            val dataSourceFactory = DefaultDataSource.Factory(context)
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                MediaItem.fromUri(url))

            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()

            if(absoluteAdapterPosition == 0)
            {
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            }

            videosPreparedListener.onVideoPrepared(
                ExoPlayerItem(
                    exoPlayer,
                    absoluteAdapterPosition
                )
            )

        }
    }


    interface OnVideosPreparedListener{
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }

}