package com.geek.booklog.ui


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.geek.booklog.R
import com.geek.booklog.bookLogApp
import com.geek.booklog.model.Author
import com.geek.booklog.model.Book
import io.realm.Realm
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.fragment_add_book.*
import timber.log.Timber
import kotlin.collections.ArrayList


class AddBookFragment : Fragment() {

        private lateinit var realmClass: Realm
        private var selectedItems: ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = SyncConfiguration.Builder(bookLogApp.currentUser(), "PUBLIC")
            .build()

        Realm.getInstanceAsync(config, object: Realm.Callback(){
            override fun onSuccess(realm: Realm) {
                realmClass = realm
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_book, container, false)
    }

    override fun onStart() {
        super.onStart()

        button_addBook.setOnClickListener{
            addBook()
        }

        authorsBox.setOnClickListener{
            loadAuthors()
        }
    }

    private fun loadAuthors() {
        realmClass.executeTransactionAsync{
            val authorList = it.where(Author::class.java).sort("name").findAll()
            val nameList = ArrayList<String>()
            authorList.toTypedArray().map{obj->
                nameList.add(obj.name)
            }
            openDialogBox(nameList)
        }
    }

    private fun openDialogBox(authorList: ArrayList<String>) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Author/s")
        val selectedAuthors = BooleanArray(authorList.size)

        builder.setMultiChoiceItems(authorList, selectedAuthors) { dialog, which, isChecked ->
            if (isChecked) {
                //when checkbox selected, add position
                selectedItems.add(which)
            } else if (selectedItems.contains(which)) {
                //when checkbox unselected
                //remove pos from list
                selectedItems.remove(which)
            }
        }
        builder.setPositiveButton("OK") { dialog, which ->
            selectedItems.forEach{
                Timber.d("Authors, ${authorList[it]}")
            }
        }
            .setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, id ->
                    dialog.dismiss()
            })

        builder.create()
    }

    fun onRadioButtonClicked(view:View){

    }

    private fun addBook() {

        if(!validateInfo()){
            addBookFailed("Fields cannot be empty")
        }
        val bookName =  bookName.text.toString()
    }

    private fun validateInfo(): Boolean = when {
        bookName.text.isEmpty() -> false
        else ->true

    }

    private fun addBookFailed(errorMsg: String){
        Timber.e(errorMsg)
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        realmClass.close()
    }
}
