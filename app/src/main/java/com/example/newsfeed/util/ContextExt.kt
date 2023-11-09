package com.example.newsfeed.util

import android.content.Context
import android.net.ConnectivityManager

fun Context.isNetworkEnabled(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetwork != null
}