package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun setLoggedIn(logged: Boolean) {
        prefs.edit().putBoolean("isLoggedIn", logged).apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("isLoggedIn", false)

    fun logout() {
        prefs.edit().clear().apply()
    }
}