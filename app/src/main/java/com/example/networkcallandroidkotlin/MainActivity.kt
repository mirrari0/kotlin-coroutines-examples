package com.example.networkcallandroidkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        ThreadedRunBlockButton.setOnClickListener {
            getMessageThreadedRunblocking()
        }
        asyncButton.setOnClickListener {
            getMessageAsyncAwait()
        }
        UnthreadedRunblockButton.setOnClickListener {
            getMessageNotThreadedRunblocking()
        }
    }

    private fun getMessageAsyncAwait() {
        var message = "fetching..."
        OutputDisplay.text = message

        GlobalScope.async(Dispatchers.Main) {
            val response = GlobalScope.async(newSingleThreadContext("NetworkCall")) {
                calloutToHelloWorldEndpoint()
            }
            val jsonMsg = parseHelloWorldJson(response)
            message = "Async Only Result: $jsonMsg"
            OutputDisplay.text = message

        }
    }


    private fun getMessageThreadedRunblocking() {
        var message = "fetching..."
        OutputDisplay.text = message

        val response = GlobalScope.async(Dispatchers.IO) {
            calloutToHelloWorldEndpoint()
        }
        GlobalScope.async(newSingleThreadContext("Runblocking")) {
            runBlocking {
                message = "RunBlocked Result: " + parseHelloWorldJson(response)
                OutputDisplay.text = message
            }
        }
    }

    // Will never display the fetching because the runblocking stops the current thread from finishing (updating and rerendering the display text) until the runblock stops
    private fun getMessageNotThreadedRunblocking() {
        var message = "fetching..."
        OutputDisplay.text = message

        val response = GlobalScope.async(Dispatchers.IO) {
            calloutToHelloWorldEndpoint()
        }
        runBlocking {
            message = "RunBlocked Result: " + parseHelloWorldJson(response)
            OutputDisplay.text = message
        }
    }

    @WorkerThread
    private fun calloutToHelloWorldEndpoint(): Response {
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
