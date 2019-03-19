package com.example.networkcallandroidkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
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

        val response: Deferred<Response> = GlobalScope.async {
            OkHttpClient()
                .newCall(
                    Request.Builder()
                        .url(HELLO_WORLD_ENDPOINT)
                        .build()
                )
                .execute()
        }

        var message = "fetching..."
        OutputDisplay.setText(message)
        runBlocking {
            message = "RunBlocked Result: " + parseHelloWorldJson(response)
            OutputDisplay.setText(message)
        }
    }

    private suspend fun parseHelloWorldJson(response: Deferred<Response>): String {
        return JSONObject(
            response.await()
                .body()?.string()
        ).getString("message")
    }
}
