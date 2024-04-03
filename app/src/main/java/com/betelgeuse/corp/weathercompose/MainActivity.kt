package com.betelgeuse.corp.weathercompose

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.betelgeuse.corp.weathercompose.data.WeatherModel
import com.betelgeuse.corp.weathercompose.screens.DialogSearch
import com.betelgeuse.corp.weathercompose.screens.MainCard
import com.betelgeuse.corp.weathercompose.screens.TabLayout
import com.betelgeuse.corp.weathercompose.ui.theme.WeatherComposeTheme
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

const val API_KEY = "cbe1ae43a1f5de2295dd0f3b91beea91"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherComposeTheme {
                val daysList = remember {
                    mutableStateOf(listOf<WeatherModel>())
                }
                val dialogState = remember {
                    mutableStateOf(false)
                }

                val currentDay = remember {
                    mutableStateOf(WeatherModel(
                        "",
                        "",
                        "0.0",
                        "",
                        "",
                        "0.0",
                        "0.0",
                        "",
                    ))
                }
                if (dialogState.value){
                    DialogSearch(dialogState, onSubmit = {
                        getData(it, this, daysList, currentDay)
                    })
                }
                getData("London", this, daysList, currentDay)
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "backImage",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                Column {
                    MainCard(currentDay, onClickSync = {
                        getData("London", this@MainActivity, daysList, currentDay)
                    }, onClickSearch = {
                        dialogState.value = true
                    }
                    )
                    TabLayout(daysList, currentDay)
                }
            }
        }
    }
}

private fun getData(name: String, context: Context,
                    daysList: MutableState<List<WeatherModel>>, currentDay:MutableState<WeatherModel>) {
    val langs = "ru" // Устанавливаем русский язык
//    val url = "https://api.openweathermap.org/data/2.5/forecast?q=$name&$lang=fr&appid=$API_KEY&units=metric&cnt=5"
    val url = "https://api.openweathermap.org/data/2.5/forecast?lat=57&lon=-2.15&appid=$API_KEY&units=metric&cnt=32"
    val queue = Volley.newRequestQueue(context)
    val sRequest = StringRequest(
        com.android.volley.Request.Method.GET, url,
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

private fun getWeatherByDays(response: String): List<WeatherModel> {
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
