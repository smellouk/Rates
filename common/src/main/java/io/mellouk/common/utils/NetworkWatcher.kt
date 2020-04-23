package io.mellouk.common.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi


class NetworkWatcher(
    private val context: Context
) {
    private val listeners = mutableListOf<ConnectivityListener>()
    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private var connectivityReceiver: ConnectivityReceiver = ConnectivityReceiver(listeners)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private var networkCallback: NetworkCallback = NetworkCallback(listeners)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    fun star() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        } else {
            context.registerReceiver(
                connectivityReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    fun stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } else {
            context.unregisterReceiver(connectivityReceiver)
        }
    }

    fun registerListener(listener: ConnectivityListener) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: ConnectivityListener) {
        listeners.remove(listener)
    }
}

private class ConnectivityReceiver(
    private val listeners: List<ConnectivityListener>
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            listeners.forEach { listener ->
                listener.onNetworkChanged(isNetworkAvailable(context))
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo ?: return false
        return activeNetworkInfo.isConnected
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
private class NetworkCallback(
    private val listeners: List<ConnectivityListener>
) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network?) {
        listeners.forEach { listener ->
            listener.onNetworkChanged(true)
        }
    }

    override fun onLost(network: Network?) {
        listeners.forEach { listener ->
            listener.onNetworkChanged(false)
        }
    }
}

interface ConnectivityListener {
    fun onNetworkChanged(isConnected: Boolean)
}