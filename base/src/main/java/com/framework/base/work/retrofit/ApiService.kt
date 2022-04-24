package com.framework.base.work.retrofit

import com.framework.base.model.UserInfo
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // 获取刈割情况详细信息
    @POST("/bre/records/{id}")
    fun addUserInfo(@Body grassYield: UserInfo): Call<ResponseDataBean<Long>>?

//    @Multipart
//    @POST("/tool/uploadMulterFile")
//    fun uploadImage(@Part part: MultipartBody.Part?): Call<CommonPicture?>?

    // 获取刈割情况详细信息
    @GET("/bre/records/{id}")
    fun getMowingDetail(@Path("id") id: String): Call<ResponseDataBean<Any>>?

//    // 新增豆科种子产量
//    @POST("/bre/leguminous")
//    fun addLeguminosaeSeed(@Body grassYield: LeguminosaeSeed): Call<ResponseDataBean<Any>>?
//
//    // 获取people
//    @GET("/user/list")
//    fun getPeoples(): Call<ResponseDataBean<List<People>>>?
//
//    // adCode
//    @GET("https://restapi.amap.com/v3/geocode/regeo")
//    fun getAdCode(
//        @Query("location") location: String,
//        @Query("output") output: String = "json",
//        @Query("key") key: String = KEY_GAODE
//    ): Call<LocationAdCode>?
//
//    // 天气
//    @GET("https://restapi.amap.com/v3/weather/weatherInfo")
//    fun getWeather(
//        @Query("city") city: String,
//        @Query("key") key: String = KEY_GAODE
//    ): Call<Weather>?
//
//    // 经纬度获取位置
//    @GET("https://restapi.amap.com/v3/geocode/regeo")
//    fun getLocation(
//        @Query("location") location: String,
//        @Query("key") key: String = KEY_GAODE
//    ): Call<LocationAdCode>?
}