package com.geek.booklog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.mongodb.User
import timber.log.Timber

class LibraryActivity : AppCompatActivity() {

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

    }

    override fun onStart() {
        super.onStart()

        user = bookLogApp.currentUser()
        Timber.d("User is $user")
        if (user == null) {
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}