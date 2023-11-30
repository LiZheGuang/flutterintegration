package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import android.util.Log
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterFragment
//import io.flutter.embedding.android.*;
import  android.content.Context
import androidx.core.content.ContextCompat.startActivity
import android.os.Handler
class MainActivity : ComponentActivity() {
    fun openFlutterActivity() {
        val intent = FlutterActivity.createDefaultIntent(this)
//        startActivity(intent)
//         val handler = Handler()

//         handler.postDelayed({
             startActivity(intent)
//         }, 5000) // 5000 milliseconds = 5 seconds
    }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android111", modifier = Modifier.offset(45.dp, 24.dp))
                    Greeting("demo")
                    DemoButton(::openFlutterActivity)
//                    openFlutterActivity()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!！！",
        modifier = modifier
    )
}

@Composable
fun DemoButton(action:() -> Unit) {
    // 创建一个可变的Boolean状态值
    var buttonText by rememberSaveable { mutableStateOf("Click Me") }

    // 使用`Button`组件创建一个按钮
    Button(
        onClick = {
            // 在点击时更新按钮文本
            buttonText = "Clicked!"
            action()
        },
        modifier = Modifier.padding(16.dp)
    ) {
        // 展示当前按钮文本
        Text(text = buttonText)
    }
}

// 模拟一个在按钮点击时执行的操作



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}