package com.betelgeuse.corp.weathercompose.View

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
import com.betelgeuse.corp.weathercompose.Model.WeatherModel
import com.betelgeuse.corp.weathercompose.R
import com.betelgeuse.corp.weathercompose.ViewModel.getData
import com.betelgeuse.corp.weathercompose.View.screens.DialogSearch
import com.betelgeuse.corp.weathercompose.View.screens.MainCard
import com.betelgeuse.corp.weathercompose.View.screens.TabLayout
import com.betelgeuse.corp.weathercompose.ui.theme.WeatherComposeTheme
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
                    mutableStateOf(
                        WeatherModel(
                        "",
                        "",
                        "0.0",
                        "",
                        "",
                        "0.0",
                        "0.0",
                        "",
                    )
                    )
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
