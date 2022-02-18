package com.geek.booklog

import android.app.Application
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import timber.log.Timber

const val appId = "booklog-zhnpl"
lateinit var bookLogApp: App

class BookLogApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        //RealmLog.setLevel(LogLevel.ALL)

        Timber.plant(Timber.DebugTree())


        bookLogApp = App(AppConfiguration.Builder(appId).build())

    }

}