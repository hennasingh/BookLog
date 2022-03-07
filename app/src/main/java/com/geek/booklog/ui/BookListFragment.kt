package com.geek.booklog.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.geek.booklog.LoginActivity
import com.geek.booklog.bookLogApp
import com.geek.booklog.databinding.FragmentBookListBinding
import com.geek.booklog.realmmodel.BookRealm
import com.geek.booklog.realmmodel.BookListAdapter
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import timber.log.Timber


class BookListFragment : Fragment() {

    private lateinit var realmList: Realm
    private lateinit var adapter: BookListAdapter


    private var user: User? = null
     var listBinding: FragmentBookListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        user = bookLogApp.currentUser()

        Timber.d("User is $user")
        if (user == null) {
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        } else {

            val config = SyncConfiguration.Builder(
                bookLogApp.currentUser(),
                "PUBLIC"
            ).build()

            //        Realm.getInstanceAsync(config, object:Realm.Callback(){
//            override fun onSuccess(realm: Realm) {
//                Timber.d("Realm downloaded")
//                realmList = realm
//            }
//        })

            realmList = Realm.getInstance(config)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Timber.d("onCreateView")
        listBinding = FragmentBookListBinding.inflate(layoutInflater, container, false)
        return listBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listBinding?.rvBookList?.layoutManager = LinearLayoutManager(context)
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
       adapter = BookListAdapter(realmList.where(BookRealm::class.java).sort("name").findAll())
        listBinding?.rvBookList?.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        // its recommended to close realm references even if the user does not logout
        realmList.close()
    }

}