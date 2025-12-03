package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalc.ui.theme.TipCalcTheme
import kotlinx.coroutines.launch

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

fun calcTipAmount(sum: Double, tipPercente: Int): Double {
    return sum * tipPercente / 100.0
}

fun calcDishDiscountPercent(dishes: Int): Int {
    return when (dishes) {
        in 1..2 -> 3
        in 3..5 -> 5
        in 6..10 -> 7
        in 11..Int.MAX_VALUE -> 10
        else -> 0
    }
}

fun calcDiscountAmount(sum: Double, discountPercent: Int): Double {
    return sum * discountPercent / 100.0
}

@Composable
fun DiscountRadioDisplay(percent: Int, selected: Boolean) {
    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        RadioButton(selected = selected, onClick = null, enabled = false)
        Text(text = "$percent%")
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

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Чаевые:")
        var sliderValue by remember { mutableStateOf(0f) } // 0..25
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("0")
            Text("25")
        }
        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                val sum = sumText.replace(',', '.').toDoubleOrNull() ?: 0.0
                val tip = calcTipAmount(sum, sliderValue.toInt())
                // показываем Snackbar
                scope.launch {
                    snackbarHostState.showSnackbar("Чаевые: ${String.format("%.2f", tip)}")
                    }
                },
                valueRange = 0f..25f,
                steps = 4, // 5 шагов: 0,5,10,15,20,25 -> steps = (25/5)-1 = 4
                modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                SnackbarHost(hostState = snackbarHostState)



        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Скидка:")
        val dishes = dishesText.toIntOrNull() ?: 0
        val discountPercentByDishes = calcDishDiscountPercent(dishes)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            DiscountRadioDisplay(percent = 3, selected = discountPercentByDishes == 3)
            DiscountRadioDisplay(percent = 5, selected = discountPercentByDishes == 5)
            DiscountRadioDisplay(percent = 7, selected = discountPercentByDishes == 7)
            DiscountRadioDisplay(percent = 10, selected = discountPercentByDishes == 10)
        }

        Spacer(modifier = Modifier.height(16.dp))
        var displayField by remember { mutableStateOf("") }
        val sum = sumText.replace(',', '.').toDoubleOrNull() ?: 0.0
        val selectedDiscountPercent = discountPercentByDishes
        val discountAmount = calcDiscountAmount(sum, selectedDiscountPercent)
        val tipAmount = calcTipAmount(sum, sliderValue.toInt())
        LaunchedEffect(sum, selectedDiscountPercent) {
            displayField = String.format("Скидка: %.2f", discountAmount)
        }
        OutlinedTextField(
            value = displayField,
            onValueChange = {  },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text("Сумма скидки / Итог") }
        )

    }

}