package br.com.thiagosantos.statusbarloading

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {

    private var receiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        val rootLayout = RootLayout(this)

        setContentView(rootLayout)

        val handler = Handler(Looper.getMainLooper(), Handler.Callback {

            var connected = false

            if (it.what == 123456789) {
                connected = it.obj as Boolean
                if (connected) {
                    rootLayout.hideStatus(window)
                } else {
                    rootLayout.showStatus(window)
                }
            }

            connected
        })

        receiver = ConnectionBroadcastReceiver(handler)
    }

    override fun onStart() {
        super.onStart()

        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {

        unregisterReceiver(receiver)

        super.onStop()
    }
}
