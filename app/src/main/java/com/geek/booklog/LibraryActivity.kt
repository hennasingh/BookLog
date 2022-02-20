package com.geek.booklog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import android.os.Bundle
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_library.*
import timber.log.Timber

class LibraryActivity : AppCompatActivity() {

    //private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        setUpViews()

    }

    private fun setUpViews() {

        //Finding the Navigation Controller
        val navController = findNavController(R.id.fragNavHost)

        // Setting Navigation Controller with the BottomNavigationView
        bottomNavView.setupWithNavController(navController)

        // Setting Up ActionBar with Navigation Controller
        // Pass the IDs of top-level destinations in AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf (
                R.id.bookListFragment,
                R.id.addBookFragment,
                R.id.addOwnerFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

//    override fun onStart() {
//        super.onStart()
//
//        user = bookLogApp.currentUser()
//        Timber.d("User is $user")
//        if (user == null) {
//            // if no user is currently logged in, start the login activity so the user can authenticate
//            startActivity(Intent(this, LoginActivity::class.java))
//        }
    }
