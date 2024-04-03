package com.betelgeuse.corp.weathercompose.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.betelgeuse.corp.weathercompose.data.WeatherModel
import com.betelgeuse.corp.weathercompose.ui.theme.BlueBack

@Composable
fun MainList(list: List<WeatherModel>, currentDay: MutableState<WeatherModel>){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
        itemsIndexed(
            list
        ){
            _, item ->
            ListItem(item, currentDay)
        }
    }
}

@Composable
fun ListItem(item: WeatherModel, currentDay: MutableState<WeatherModel>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp, start = 4.dp, end = 4.dp)
            .clickable {
                currentDay.value = item
            },
        colors = CardDefaults.cardColors(containerColor = BlueBack),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
            ) {
                Text(text = item.time)
                Text(
                    text = item.condition,
                    color = Color.White
                )
            }
            Text(
                text = item.currentTemp.ifEmpty { "${item.maxTemp}/${item.minTemp}" },
                color = Color.White,
                fontSize = 40.sp
            )
            AsyncImage(
                model = "https:${item.icon}",
                contentDescription = "weatherStatus2",
                modifier = Modifier
                    .size(20.dp)
                    .padding(
                        end = 8.dp
                    )
            )
        }
    }
}

@Composable
fun DialogSearch(dialogState: MutableState<Boolean>, onSubmit: (String) -> Unit){
    val dialogText = remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = {
        dialogState.value = false
    },
        confirmButton = {
            TextButton(onClick = {
                onSubmit(dialogText.value)
                dialogState.value = false
            }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                dialogState.value = false
            }) {
                Text(text = "Cancel")
            }
        },
        title = {
            Column (modifier = Modifier.fillMaxWidth()){
                Text(text = "Введите название города")
                TextField(value = dialogText.value, onValueChange = {
                    dialogText.value = it
                })
            }
        }
    )
}