package com.orhanucar.weatherapp.view
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orhanucar.weatherapp.R
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.orhanucar.weatherapp.databinding.ActivityMainBinding
import com.orhanucar.weatherapp.viewmodel.MainViewModel

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewmodel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val edtCityName = binding.edtCityName
        val swipeRefreshLayout = binding.swipeRefreshLayout
        val llData = binding.llData
        val tvError = binding.tvError
        val pbLoading = binding.pbLoading
        val imgSearchCity = binding.imgSearchCity

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        var cName = GET.getString("cityName", "bingöl")?.toLowerCase()
        edtCityName.setText(cName)
        viewmodel.refreshData(cName!!)

        getLiveData()

        swipeRefreshLayout.setOnRefreshListener {
            llData.visibility = View.GONE
            tvError.visibility = View.GONE
            pbLoading.visibility = View.GONE

            var cityName = GET.getString("cityName", cName)?.toLowerCase()
            edtCityName.setText(cityName)
            viewmodel.refreshData(cityName!!)
            swipeRefreshLayout.isRefreshing = false
        }

        imgSearchCity.setOnClickListener {
            val cityName = edtCityName.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewmodel.refreshData(cityName)
            getLiveData()
            Log.i(TAG, "onCreate: " + cityName)
        }

    }

    private fun getLiveData() {

        val tvCityCode = binding.tvCityCode
        val tvCityName = binding.tvCityName
        val imgWeatherPictures = binding.imgWeatherPictures
        val tvDegree = binding.tvDegree
        val tvHumidity = binding.tvHumidity
        val tvWindSpeed = binding.tvWindSpeed
        val tvLat = binding.tvLat
        val tvLon = binding.tvLon
        val llData = binding.llData

        viewmodel.weather_data.observe(this, Observer { data ->
            data?.let {
                llData.visibility = View.VISIBLE

                tvCityCode.text = data.sys.country.toString()
                tvCityName.text = data.name.toString()

                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    .into(imgWeatherPictures)

                var degree = data.main.temp/10
                tvDegree.text = degree.toString() + "°C"

                tvHumidity.text = data.main.humidity.toString() + "%"
                tvWindSpeed.text = data.wind.speed.toString()
                tvLat.text = data.coord.lat.toString()
                tvLon.text = data.coord.lon.toString()

            }
        })

        viewmodel.weather_error.observe(this, Observer { error ->
            error?.let {
                if (error) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.pbLoading.visibility = View.GONE
                    llData.visibility = View.GONE
                } else {
                    binding.tvError.visibility = View.GONE
                }
            }
        })

        viewmodel.weather_loading.observe(this, Observer { loading ->
            loading?.let {
                if (loading) {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    llData.visibility = View.GONE
                } else {
                    binding.pbLoading.visibility = View.GONE
                }
            }
        })

    }
}