package com.orhanucar.weatherapp.service

import com.orhanucar.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("data/your API")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>
}