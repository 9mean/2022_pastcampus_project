package com.example.cloneyoutube

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloneyoutube.adapter.VideoAdapter
import com.example.cloneyoutube.databinding.FragmentPlayerBinding
import com.example.cloneyoutube.service.VideoService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayerFragment: Fragment(R.layout.fragment_player) {
    private var binding:FragmentPlayerBinding?=null
    private lateinit var videoAdapter: VideoAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentPlayerBinding=FragmentPlayerBinding.bind(view)
        binding=fragmentPlayerBinding
        initMotionLayoutEvent(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)
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
        binding?.let {
            it.playerMotionLayout.transitionToEnd()
            it.bottomTitleTextView.text=title
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}