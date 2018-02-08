package br.com.thiagosantos.statusbarloading

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Message
import android.support.v4.net.ConnectivityManagerCompat

class ConnectionBroadcastReceiver(private val handler: Handler) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ac = ConnectivityManagerCompat.getNetworkInfoFromBroadcast(cm, intent)

        val connected = ac?.isConnected ?: false

        val msg = Message()
        msg.what = 123456789
        msg.obj = connected

        handler.sendMessage(msg)
    }

}