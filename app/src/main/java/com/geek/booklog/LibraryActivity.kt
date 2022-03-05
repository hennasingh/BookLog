package com.geek.booklog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.geek.booklog.databinding.ActivityLibraryBinding
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_library.*
import timber.log.Timber

class LibraryActivity : AppCompatActivity() {

    private var user: User? = null
    private lateinit var binding: ActivityLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = bookLogApp.currentUser()

        Timber.d("User is $user")
        if (user == null) {
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
        }else {

            setUpViews()
        }
    }

    private fun setUpViews() {

        //Finding the Navigation Controller
       val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        // Setting Navigation Controller with the BottomNavigationView
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

        // Setting Up ActionBar with Navigation Controller
        // Pass the IDs of top-level destinations in AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.bookListFragment,
                R.id.addBookFragment,
                R.id.addAuthorFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

}
