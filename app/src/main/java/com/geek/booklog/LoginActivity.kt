package com.geek.booklog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_login.setOnClickListener{
            login(false)
        }
        button_create.setOnClickListener{
            login(true)
        }
    }
    private fun login(createUser: Boolean) {

        if(!validateCredentials()){
            onLoginFailed("Fields cannot be empty")
            return
        }

        // while this operation completes, disable the buttons to login or create a new account
        button_login.isEnabled = false
        button_create.isEnabled = false

        val email = username.text.toString()
        val password = password.toString()


        if(createUser){
            //register  a user
            bookLogApp.emailPassword.registerUserAsync(email, password){
                if(it.isSuccess){
                    Timber.d("User successfully registered")
                    // when the account has been created successfully, log in to the account
                    login(false)
                }else {
                    onLoginFailed(it.error.errorMessage ?: "An error occurred on registering")
                    enableButtons()
                }
            }

        } else {
            val credentials = Credentials.emailPassword(email, password)
            bookLogApp.loginAsync(credentials){
                if(!it.isSuccess){
                    enableButtons()
                    onLoginFailed(it.error.errorMessage ?: "An error occurred")
                } else {
                    onLoginSuccess()
                }
            }
        }
    }

    private fun enableButtons() {
        button_create.isEnabled = true
        button_login.isEnabled = true
    }

    private fun onLoginSuccess() {
        //Start Next Activity
        finish()
    }

    private fun validateCredentials(): Boolean = when {
        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.
        username.text.toString().isEmpty() -> false
        password.text.toString().isEmpty() -> false
        else -> true
    }


    private fun onLoginFailed(errorMsg: String) {
        Timber.e(errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }
}