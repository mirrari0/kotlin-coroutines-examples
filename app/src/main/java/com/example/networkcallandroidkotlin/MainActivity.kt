package com.example.networkcallandroidkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.WorkerThread
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private val HELLO_WORLD_ENDPOINT = "https://secret-wave-44234.herokuapp.com/helloworld"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RunBlockButton.setOnClickListener {
            getMessageRunblocking()
        }
    }


    private fun getMessageRunblocking() {
        var message = "fetching..."
        OutputDisplay.text = message

        val response = GlobalScope.async(Dispatchers.IO) {
            calloutToHelloWorldEndpoint()
        }

        GlobalScope.async(Dispatchers.Main) {
            runBlocking {
                message = "RunBlocked Result: " + parseHelloWorldJson(response)
            }
            OutputDisplay.text = message

        }
    }

    @WorkerThread
    private fun calloutToHelloWorldEndpoint():Response {
        return OkHttpClient()
            .newCall(
                Request.Builder()
                    .url(HELLO_WORLD_ENDPOINT)
                    .build()
            )
            .execute()
    }

    private suspend fun parseHelloWorldJson(response: Deferred<Response>): String {
        return JSONObject(
            response.await()
                .body()?.string()
        ).getString("message")
    }
}
