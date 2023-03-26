package com.example.musicplayerapp

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.postDelayed
import com.example.musicplayerapp.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var finalTime: Double = 0.0
    var startTime: Double = 0.0
    var forwardTime = 10000
    var backWardTime = 10000
    val handler = Handler(Looper.getMainLooper())
    lateinit var mediaPlayer: MediaPlayer

    var oneTimeOnly = 0
    var isPlaying: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaPlayer = MediaPlayer.create(this, R.raw.falling)
        initialize()
    }

    private fun initialize() {
        with(binding) {
            seekBar.isClickable = false
            //Add buttons functionalities
            pauseBtn.setOnClickListener {
                //Pause the music
                isPlaying = false
                mediaPlayer.pause()
                pauseBtn.visibility = View.INVISIBLE
                playBtn.visibility = View.VISIBLE
            }
            playBtn.setOnClickListener {
                //Start the music
                mediaPlayer.start()
                finalTime = mediaPlayer.duration.toDouble()
                startTime = mediaPlayer.currentPosition.toDouble()
                isPlaying = true

                playBtn.visibility = View.INVISIBLE
                pauseBtn.visibility = View.VISIBLE
                if (oneTimeOnly == 0) {
                    seekBar.max = finalTime.toInt()
                    oneTimeOnly = 1
                }

                remainingTime.text = startTime.toString()
                seekBar.progress = startTime.toInt()
                handler.postDelayed({ updateSongTime }, 1000)

            }

            songTitle.text = resources.getResourceEntryName(R.raw.falling)
            forwardBtn.setOnClickListener {
                var temp = startTime
                if ((temp + forwardTime) <= finalTime) {
                    startTime += forwardTime
                    mediaPlayer.seekTo(startTime.toInt())
                } else {
                    Toast.makeText(this@MainActivity, "Can't jump forward", Toast.LENGTH_LONG)
                        .show()
                }
            }

            backBtn.setOnClickListener {
                var temp = startTime.toInt()

                if ((temp - backWardTime) > 0) {
                    startTime -= backWardTime
                    mediaPlayer.seekTo(startTime.toInt())
                } else {
                    Toast.makeText(this@MainActivity, "Can't jump backward", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    val updateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            binding.remainingTime.text = "" + String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(
                        startTime.toLong()
                    )
                )
            )
            binding.seekBar.progress = startTime.toInt()
            handler.postDelayed(this, 1000)
        }
    }

}