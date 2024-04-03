package com.betelgeuse.corp.weathercompose.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.betelgeuse.corp.weathercompose.Model.WeatherModel
import com.betelgeuse.corp.weathercompose.View.API_KEY


fun getData(name: String, context: Context,
                    daysList: MutableState<List<WeatherModel>>, currentDay: MutableState<WeatherModel>
) {
    val langs = "ru" // Устанавливаем русский язык
//    val url = "https://api.openweathermap.org/data/2.5/forecast?q=$name&$lang=fr&appid=$API_KEY&units=metric&cnt=5"
    val url = "https://api.openweathermap.org/data/2.5/forecast?lat=57&lon=-2.15&appid=$API_KEY&units=metric&cnt=32"
    val queue = Volley.newRequestQueue(context)
    val sRequest = StringRequest(
        Request.Method.GET, url,
        { response ->
            val list = getWeatherByDays(response)
            currentDay.value = list[0]
            daysList.value = list
        },
        { error ->
            Log.d("MyLog", "Volley error: $error")
        }
    )
    queue.add(sRequest)
}