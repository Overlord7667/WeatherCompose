package com.betelgeuse.corp.weathercompose.View.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.betelgeuse.corp.weathercompose.R
import com.betelgeuse.corp.weathercompose.Model.WeatherModel
import com.betelgeuse.corp.weathercompose.ui.theme.BlueBack
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


@Composable
fun MainCard(currentDay: MutableState<WeatherModel>, onClickSync: () -> Unit, onClickSearch: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = BlueBack
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            ),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            start = 8.dp
                        ),
                        text = currentDay.value.time,
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White
                    )
                    AsyncImage(
                        model = "https:" + currentDay.value.icon,
                        contentDescription = "weatherStatus",
                        modifier = Modifier
                            .size(75.dp)
                            .padding(
                                top = 3.dp,
                                end = 8.dp
                            )
                    )
                }
                Text(
                    text = currentDay.value.city,
                    style = TextStyle(fontSize = 25.sp),
                    color = Color.White
                )
                Text(
                    text = currentDay.value.currentTemp.toFloat().toInt().toString() + "°C",
                    style = TextStyle(fontSize = 65.sp),
                    color = Color.White
                )
                Text(
                    text = currentDay.value.condition,
                    style = TextStyle(fontSize = 17.sp),
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            onClickSearch.invoke()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "searchBtn",
                            tint = Color.White
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = "${currentDay.value.maxTemp.toFloat().toInt()}°C/${currentDay.value.minTemp.toFloat().toInt()}°C",
                        style = TextStyle(fontSize = 17.sp),
                        color = Color.White
                    )
                    IconButton(
                        onClick = {
                            onClickSync.invoke()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cloud_sync),
                            contentDescription = "syncBtn",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(daysList: MutableState<List<WeatherModel>>, currentDay: MutableState<WeatherModel>) {
    val tabList = listOf(
        "Hours", "Days"
    )
    val pagerState = rememberPagerState()
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.clip(RoundedCornerShape(5.dp))
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            indicator = {position ->
                TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(position[tabIndex]),
                height = 3.dp,
                    Color.White
            )
            },
            contentColor = Color.White,
            containerColor = BlueBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    selected = false,
                    onClick = {
                              coroutineScope.launch {
                                  pagerState.animateScrollToPage(index)
                              }
                    },
                    text = {
                        Text(
                            text = text
                        )
                    }
                )
            }
        }
        HorizontalPager(
            count = tabList.size,
            state = pagerState,
            modifier = Modifier.weight(1.0f)
        ) {
            index ->
            val list = when(index){
                0 -> getWeatherByHours(currentDay.value.hours)
                1 -> daysList.value
                else -> daysList.value
            }
            MainList(list, currentDay)
        }
    }
}

private fun getWeatherByHours(hours: String): List<WeatherModel>{
    if(hours.isEmpty()) return listOf()
    val hoursArray = JSONArray(hours)
    val list = ArrayList<WeatherModel>()
    for (i in 0 until hoursArray.length()){
        val item = hoursArray[i] as JSONObject
        list.add(
            WeatherModel(
                "",
                item.getString("dt_txt"),
                item.getString("temp").toFloat().toInt().toString() + "C",
                item.getJSONObject("weather").getString("main"),
                item.getJSONObject("weather").getString("icon"),
                "",
                "",
                ""
            )
        )
    }
    return list
}
