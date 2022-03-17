package com.geek.booklog.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.geek.booklog.R
import com.geek.booklog.bookLogApp
import com.geek.booklog.realmmodel.Author
import io.realm.Realm
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.fragment_add_author.*
import org.bson.types.ObjectId
import timber.log.Timber
import java.util.concurrent.TimeUnit


class AddAuthorFragment : Fragment() {


    private lateinit var realmClass: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_author, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = SyncConfiguration.Builder(bookLogApp.currentUser(), bookLogApp.currentUser()?.id)
            .waitForInitialRemoteData(500, TimeUnit.MILLISECONDS)
            .build()

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                realmClass = realm
            }
        })
    }

    private fun addAuthor() {

        if(!validateText()){
            addAuthorFailed("Field cannot be empty")
            return
        }
        val authorName = authorName.text.toString()
        createAuthorObject(authorName)
    }

    private fun createAuthorObject(authorName: String) {

        realmClass.executeTransactionAsync({
            val author = it.createObject(Author::class.java, ObjectId())
            //configure the instance
            author.name = authorName
        }, {
            Timber.d("Successfully Added")
            Toast.makeText(context, "Author successfully added", Toast.LENGTH_LONG).show()
        }, {
            Timber.e("Error adding Author %s",it.localizedMessage)
            Toast.makeText(context, "Author cannot be added, Try again!", Toast.LENGTH_LONG).show()
        })
    }

    private fun validateText(): Boolean = when{
        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.
        authorName.text.toString().isEmpty() -> false
        else -> true
    }

    private fun addAuthorFailed(errorMsg: String) {
        Timber.e(errorMsg)
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        realmClass.close()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_add.setOnClickListener{
            addAuthor()
        }
    }
}