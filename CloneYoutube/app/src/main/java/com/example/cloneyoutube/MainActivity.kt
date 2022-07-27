package com.example.cloneyoutube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloneyoutube.adapter.VideoAdapter
import com.example.cloneyoutube.databinding.ActivityMainBinding
import com.example.cloneyoutube.service.VideoService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,PlayerFragment())
            .commit()
        videoAdapter= VideoAdapter(callback = { url,title->
            supportFragmentManager.fragments.find{it is PlayerFragment}?.let {
                (it as PlayerFragment).play(url,title)
            }
        })
        binding.mainRecyclerView.apply {
            adapter=videoAdapter
            layoutManager=LinearLayoutManager(context)
        }
        getViedoList()
    }
    private fun getViedoList(){
        val retrofit=Retrofit.Builder()
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
}