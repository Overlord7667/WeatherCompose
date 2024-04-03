package com.betelgeuse.corp.weathercompose.ViewModel

import com.betelgeuse.corp.weathercompose.Model.WeatherModel
import org.json.JSONObject

fun getWeatherByDays(response: String): List<WeatherModel> {
    if (response.isEmpty()) return emptyList()
    val list = ArrayList<WeatherModel>()
    val mainObject = JSONObject(response)
    val city = mainObject.getJSONObject("city").getString("name")
    val forecastList = mainObject.getJSONArray("list")
    for (i in 0 until forecastList.length()) {
        val forecastList = mainObject.getJSONArray("list")
        val item = forecastList.getJSONObject(i)
        val main = item.getJSONObject("main")
        val weatherArray = item.getJSONArray("weather")
        val weather = weatherArray.getJSONObject(0)
        val time = item.getString("dt_txt")
        list.add(
            WeatherModel(
                city = city,
                time = time,
                currentTemp = main.getString("temp").toFloat().toInt().toString(),
                condition = weather.getString("main"),
                icon = weather.getString("icon"),
                maxTemp = main.getString("temp_max"),
                minTemp = main.getString("temp_min"),
                hours = ""
            )
        )
    }
    return list
}