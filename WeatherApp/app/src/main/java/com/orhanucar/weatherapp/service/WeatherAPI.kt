package com.orhanucar.weatherapp.service

import com.orhanucar.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=ac55f3822d4fdcc035d4053f58c16a32
interface WeatherAPI {
    @GET("data/2.5/weather?lat=44.34&lon=10.99&appid=ac55f3822d4fdcc035d4053f58c16a32")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>
}