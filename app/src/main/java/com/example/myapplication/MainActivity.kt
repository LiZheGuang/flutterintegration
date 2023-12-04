package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MessageCodec
import io.flutter.plugin.common.StringCodec

// Android UI视图
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("这是一个原生安卓页面", modifier = Modifier.offset(45.dp, 24.dp))
                }
            }
        }
    }
}

fun isMainThread() = Looper.getMainLooper().thread == Thread.currentThread()
fun Handler.postDelayed(delay: Long, runnable: Runnable) = postDelayed(runnable, delay)
class MethodChannelActivity : FlutterActivity() {
    companion object {
        private const val CHANNEL_NAME = "com.bqt.test/base_channel"
        private const val METHOD_NAME = "getBatteryLevel"
        private const val METHOD_NAME_ONE = "onCallGetFunction"
        private  const val METHOD_NAME_TWO = "onCallNeXtSwitchBackground"
        private  const val METHOD_NAME_ROUTER = "onCallNextAddRouter"
    }
    private var mBasicMessageChannel: BasicMessageChannel<String>? = null
    //    通过方法来进行调用 MethodChanel
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        println(message = "configureFlutterEngine");
        val binaryMessenger: BinaryMessenger = flutterEngine.dartExecutor.binaryMessenger

        //        双端通讯
        val messageCodec: MessageCodec<String> = StringCodec.INSTANCE // 指定编解码器
        mBasicMessageChannel = BasicMessageChannel(binaryMessenger, "com.bqt.test/basic_channel", messageCodec)
        mBasicMessageChannel?.resizeChannelBuffer(5)
        mBasicMessageChannel?.setMessageHandler { message, reply ->
            println("onMessage, message: $message，isMainThread：${isMainThread()}") // true
            reply.reply("已收到【$message】") // 只能 reply 一次
        }
        Handler(mainLooper).apply {
            postDelayed(2000) { sendMessage() } // 可以多次调用 send
            postDelayed(5000) { sendMessage() }
        }
        //        双端通讯

        MethodChannel(binaryMessenger, CHANNEL_NAME).setMethodCallHandler { call, result ->
            when (call.method) {
                METHOD_NAME -> onCallGetBatteryLevel(result)
                METHOD_NAME_ONE ->onCallGetFunction(result)
                METHOD_NAME_TWO -> onCallNeXtSwitchBackground(result)
                METHOD_NAME_ROUTER-> onCallNextAddRouter(result)
                else -> result.notImplemented()
            }
        }
    }
    private fun sendMessage() = mBasicMessageChannel?.send("当前时间 ${System.currentTimeMillis()}") { reply ->
        println("reply: $reply, isMainThread：${isMainThread()}") // true
    }
    private fun onCallGetBatteryLevel(result: MethodChannel.Result) {
        println("onCallGetBatteryLevel:${result}");
        println("onCallGetBatteryLevel, isMainThread：${isMainThread()}") // true
        Thread {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (batteryLevel != -1) {
                result.success(batteryLevel) // 可在子线程响应请求
                println("result callback, isMainThread：${isMainThread()}") // false
            } else {
                result.error("UNAVAILABLE", "Battery level not available.", null)
            }
        }.start()
    }

    private fun onCallGetFunction(result: MethodChannel.Result) {
        println("onCallGetBatteryLevel:${result}");
        println("onCallGetBatteryLevel, isMainThread：${isMainThread()}") // true

        Thread {
            result.success("执行了原生自定义onCallGetFunction方法"); // 可在子线程响应请求
            println("result callback, isMainThread：${isMainThread()}") // false
        }.start()
    }

    // 切到后台
    private fun onCallNeXtSwitchBackground(result: MethodChannel.Result){
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)


        Thread {
            result.success("执行了原生自定义onCallNeXtSwitchBackground方法"); // 可在子线程响应请求
            println("result callback, isMainThread：${isMainThread()}") // false
        }.start()
    }

    //    路由跳转
    private fun onCallNextAddRouter(result: MethodChannel.Result){
        fun goToTargetActivity() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        goToTargetActivity()
        Thread {
            result.success("执行了原生自定义onCallNextAddRouter方法"); // 可在子线程响应请求
            println("result callback, isMainThread：${isMainThread()}") // false
        }.start()
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
        modifier = Modifier.wrapContentSize().padding(10.dp)
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