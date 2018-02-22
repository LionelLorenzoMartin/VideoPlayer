package com.example.lionel.reproductovideo

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import kotlinx.android.synthetic.main.activity_main.*


class Main2Activity : AppCompatActivity() {


    private var playbackPosition = 0
    var rtspUrl ="" //"android.resource://"+ packageName+"/raw/"+R.raw.hsm
    private lateinit var mediaController: MediaController

    private val videoContainer: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rtspUrl=getIntent().getStringExtra("ruta");
        println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww"+rtspUrl)

        val videoView = findViewById<View>(R.id.videoView) as VideoView

        videoView.setVideoURI(Uri.parse(rtspUrl))
        videoView.setMediaController(MediaController( this))


        //Environment.DIRECTORY_DOWNLOADS.endsWith("mp4")

        mediaController = MediaController(this)

        videoView.setOnPreparedListener {
            mediaController.setAnchorView(videoContainer)
            videoView.setMediaController(mediaController)
            videoView.seekTo(playbackPosition)
            videoView.start()
        }

        videoView.setOnInfoListener { player, what, extras ->
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                progressBar.visibility = View.INVISIBLE
            true
        }
    }

    /*fun getPelis() {
        val pathname = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        val sd(pathname)
    }*/

    override fun onStart() {
        super.onStart()

        val uri = Uri.parse(rtspUrl)
        videoView.setVideoURI(uri)
    }

    override fun onPause() {
        super.onPause()

        videoView.pause()
        playbackPosition = videoView.currentPosition
    }

    override fun onStop() {
        videoView.stopPlayback()

        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (outState != null) {
            outState.putInt("Position", videoView.currentPosition)
        }
        videoView.pause()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getInt("Position")
        }
        videoView.seekTo(playbackPosition)

    }


}
