package com.example.cloneyoutube.service

import com.example.cloneyoutube.DTO.VideoDTO
import retrofit2.Response
import retrofit2.http.GET

interface VideoService {
    @GET("v3/94df2a04-480e-4c88-9e36-b59db86b2e66")
    suspend fun getListVideos():Response<VideoDTO>
}