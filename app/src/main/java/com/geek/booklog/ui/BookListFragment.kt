package com.geek.booklog.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onStart() {
        super.onStart()
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

        setUpRecyclerView()
        return view;
    }

    private fun setUpRecyclerView() {
        rv_bookList.layoutManager = LinearLayoutManager(context)
       adapter = BookListAdapter(realmList.where(Book::class.java).sort("name").findAll())
        rv_bookList.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        // its recommended to close realm references even if the user does not logout
        realmList.close()
    }

}