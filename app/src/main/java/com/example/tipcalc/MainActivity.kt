package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalc.ui.theme.TipCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorApp()
            }
        }
    }


@Composable
fun TipCalculatorApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            TipScreen()
        }
    }
}

@Composable
fun TipScreen(){
    var sumText by remember { mutableStateOf("") }
    var dishesText by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Text(text = "Сумма заказа:")
        OutlinedTextField(
            value = sumText,
            onValueChange = { sumText = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Количество блюд:")
        OutlinedTextField(
            value = dishesText,
            onValueChange = { dishesText = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }

}