package com.example.cloneyoutube

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloneyoutube.adapter.VideoAdapter
import com.example.cloneyoutube.databinding.FragmentPlayerBinding
import com.example.cloneyoutube.service.VideoService
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayerFragment: Fragment(R.layout.fragment_player) {
    private var binding:FragmentPlayerBinding?=null
    private lateinit var videoAdapter: VideoAdapter
    private var player:SimpleExoPlayer?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentPlayerBinding=FragmentPlayerBinding.bind(view)
        binding=fragmentPlayerBinding
        initMotionLayoutEvent(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)
        initPlayer(fragmentPlayerBinding)
        initControlBtn(fragmentPlayerBinding)
        getViedoList()
    }
    private fun initMotionLayoutEvent(fragmentPlayerBinding: FragmentPlayerBinding){
        fragmentPlayerBinding.playerMotionLayout.setTransitionListener(object :MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                binding?.let {
                    (activity as MainActivity).also {
                        it.findViewById<MotionLayout>(R.id.main_motion_layout).progress=abs(progress)

                    }
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        } )
    }
    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding){
        videoAdapter=VideoAdapter(callback = { url,title->
            play(url,title)
        })
        fragmentPlayerBinding.fragmentRecyclerView.apply {
            adapter=videoAdapter
            layoutManager=LinearLayoutManager(context)
        }
    }
    private fun initPlayer(fragmentPlayerBinding: FragmentPlayerBinding){
        context?.let{

            player=SimpleExoPlayer.Builder(it).build()
        }
        fragmentPlayerBinding.playerView.player=player
        binding?.let{
            player?.addListener(object : Player.EventListener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if(isPlaying){
                        it.bottomPlayerControllerBtn.setImageResource(R.drawable.ic_baseline_pause_24)
                    }else{
                        it.bottomPlayerControllerBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                }
            })
        }
    }
    private fun initControlBtn(fragmentPlayerBinding: FragmentPlayerBinding){
        fragmentPlayerBinding.bottomPlayerControllerBtn.setOnClickListener {
            val player=this.player?:return@setOnClickListener
            if(player.isPlaying){
                player.pause()
            }else{
                player.play()
            }
        }
    }
    private fun getViedoList(){
        val retrofit= Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(VideoService::class.java).also {
            lifecycleScope.launch {
                val res=it.getListVideos()
                if(res.isSuccessful){
                    res.body()?.let {
                        videoAdapter.submitList(it.videos)
                    }
                }
            }
        }
    }
    fun play(url:String,title:String){
        context?.let {
            val dataSourceFactory=DefaultDataSourceFactory(it)
            val mediaSource=ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            player?.setMediaSource(mediaSource)
            player?.prepare()
            player?.play()
        }
        binding?.let {
            it.playerMotionLayout.transitionToEnd()
            it.bottomTitleTextView.text=title
        }
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding=null
        player?.release()
    }
}