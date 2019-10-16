package com.example.android.navigation


import android.app.Application

class ApplicationController : Application() {
    override fun onCreate() {
        super.onCreate()
        isDebug = true
    }

    companion object {

        var isDebug: Boolean = false
    }
}