package com.example.shorts_task.api

import com.example.shorts_task.model.APIdata
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/videos")
    suspend fun getData(@Query("page") page: Int) : APIdata

}