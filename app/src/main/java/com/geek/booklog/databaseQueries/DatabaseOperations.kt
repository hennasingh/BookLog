package com.geek.booklog.databaseQueries

import com.geek.booklog.bookLogApp
import com.geek.booklog.model.Author
import com.geek.booklog.model.Book
import io.realm.Realm
import io.realm.mongodb.sync.SyncConfiguration
import java.util.concurrent.TimeUnit

class DatabaseOperations {

    private lateinit var realmTransaction: Realm
    companion object{
        private val config = SyncConfiguration.Builder(bookLogApp.currentUser(), "PUBLIC")
            .build()
    }

    private fun openRealm(){

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                realmTransaction = realm
            }
        })
    }

    fun addAuthorsToDatabase(){

        openRealm()

        realmTransaction.executeTransactionAsync{realm->

            val rowling = realm.createObject(Author::class.java)
            rowling.name = "J.K Rowling"

            val stephanie = Author(name="Stephanie Garber")
            realm.insert(stephanie)
        }
           closeRealm()
    }


    fun addBooksToDatabase(){

        openRealm()

        realmTransaction.executeTransactionAsync{realm->

            val harryPotter = realm.createObject(Book::class.java)
            harryPotter.name = "Harry Potter and Philosopher's Stone"
            harryPotter.isRead = true

        }
        closeRealm()
    }


    fun readAuthorNames(){
        openRealm()

        realmTransaction.executeTransactionAsync{realm->


        }

    }


    private fun closeRealm() {
        realmTransaction.close()
    }

}