package com.example.networkcallandroidkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.WorkerThread
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
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

        GlobalScope.async {
            val responseMessage = "RunBlocked Result: " + parseHelloWorldJson(calloutToHelloWorldEndpoint())
            this@MainActivity.OutputDisplay.text = responseMessage

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

    private fun parseHelloWorldJson(response: Response): String {
        return JSONObject(
            response
                .body()?.string()
        ).getString("message")
    }
}
