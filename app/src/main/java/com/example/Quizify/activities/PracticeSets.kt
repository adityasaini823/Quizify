package com.example.Quizify.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.Quizify.navigation.Quizapprouter
import com.example.Quizify.navigation.Screen
import com.example.Quizify.navigation.SystemBackButtonHandler

@Composable
fun PracticeSets(names:List<String> =List(20){(it + 1).toString()}){
    val firstList = names.take(6)
    val secondList = names.drop(6)
    Surface(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(15.dp)) {
        Column {
            Text(modifier=Modifier.fillMaxWidth(),
                text = "Completed",
                fontSize =  20.sp,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            LazyColumn(modifier = Modifier
                .padding(vertical = 4.dp)
                .weight(1f)){
                items(items=firstList){name->
                    ListItem(name = name)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(modifier=Modifier.fillMaxWidth(),
                text = "Continue Pratice",
                fontSize =  20.sp,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            LazyColumn(modifier = Modifier
                .padding(vertical = 4.dp)
                .weight(1f)){
                items(items=secondList){name->
                    ListItem(name = name)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(name: String){
    Surface(
        color = Color(0xFFD7EFFE),
        modifier= Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        onClick = {Quizapprouter.navigateTo(Screen.Questionsactivity)}
    ) {
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Practice Set $name", style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ), color = Color(0xFF073042)
                )
            }
        }
    }
    SystemBackButtonHandler()


}

