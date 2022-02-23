package com.geek.booklog.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geek.booklog.R
import com.geek.booklog.bookLogApp
import com.geek.booklog.model.Book
import com.geek.booklog.model.BookListAdapter
import io.realm.Realm
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.fragment_book_list.*


class BookListFragment : Fragment() {

    private lateinit var realmList: Realm
    private lateinit var adapter: BookListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = SyncConfiguration.Builder(
            bookLogApp.currentUser(),
            "PUBLIC"
        )
            .waitForInitialRemoteData()
            .build()

        Realm.getInstanceAsync(config, object:Realm.Callback(){
            override fun onSuccess(realm: Realm) {
                realmList = realm
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_book_list, container, false)

        view.findViewById<RecyclerView>(R.id.rv_bookList).layoutManager = LinearLayoutManager(context)
        setUpRecyclerView()
        return view
    }

    private fun setUpRecyclerView() {
       adapter = BookListAdapter(realmList.where(Book::class.java).sort("name").findAll())
        rv_bookList.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        // its recommended to close realm references even if the user does not logout
        realmList.close()
    }

}