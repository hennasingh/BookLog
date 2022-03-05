package com.geek.booklog.ui


import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.geek.booklog.R
import com.geek.booklog.bookLogApp
import com.geek.booklog.databinding.FragmentAddBookBinding
import com.geek.booklog.model.Author
import com.geek.booklog.model.Book
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.mongodb.sync.SyncConfiguration
import org.bson.types.ObjectId
import timber.log.Timber
import kotlin.collections.ArrayList


class AddBookFragment : Fragment(){

        private lateinit var realmClass: Realm
         var selectedItems: ArrayList<Int> = ArrayList()
         var addBinding: FragmentAddBookBinding? = null
        var bookObject = Book()
        private val authorsToAdd: RealmList<Author> = RealmList()


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addClickListeners()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addBinding = FragmentAddBookBinding.inflate(layoutInflater,container,false)
        return addBinding?.root

    }

    private fun addClickListeners() {

        addBinding!!.authorLayout.setOnClickListener{

            if(addBinding!!.addBookName.text.isEmpty()){
                addBinding!!.bookLayout.error = "Please enter Book Name"
            }else{
                bookObject.name = addBinding!!.addBookName.text.toString()
                loadAuthors()
            }
        }

        addBinding!!.radioGroupIsRead.setOnCheckedChangeListener{ radioGroup, checkedId ->
                when(checkedId) {
                    R.id.yes -> bookObject.isRead = true
                    R.id.no ->   bookObject.isRead = false
                }
        }
        addBinding!!.buttonAddBook.setOnClickListener{
                realmClass.executeTransactionAsync ({
                    it.insert(bookObject)
                }, {

                }, {throwError ->
                    Timber.d("Error adding the author %s", throwError.localizedMessage)
                })
        }

    }

    private fun loadAuthors() {
        val nameList = ArrayList<String>()
        realmClass.executeTransactionAsync({
            val authorList = it.where(Author::class.java).sort("name").findAll()
            authorList.toTypedArray().map { obj ->
                nameList.add(obj.name)
            }
        }, {
            if(nameList.size>0) openDialogBox(nameList)
            else {
                Toast.makeText(context, "Author List is empty, please add Author Name first", Toast.LENGTH_LONG).show()
            }
        }, {
            Timber.d("Error happened while reading Author List %s", it.localizedMessage)
            Toast.makeText(context, "Error happened while reading List, ${it.localizedMessage}", Toast.LENGTH_LONG).show()
        })
    }

    private fun openDialogBox(nameList: ArrayList<String>) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Author/s")
        val selectedAuthors = BooleanArray(nameList.size)

        builder.setMultiChoiceItems(nameList.toTypedArray(), selectedAuthors) { dialog, which, isChecked ->
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
                Timber.d("Authors, ${nameList[it]}")
                realmClass.executeTransactionAsync({ realm ->
                   realm.where(Author::class.java).equalTo("name", nameList[it]).findAll().map{authortoAdd ->
                       authorsToAdd.add(authortoAdd)
                   }
                }, {
                    Timber.d("Author added successfully")
                }, { throwable ->
                    Timber.d("Error adding the author %s", throwable.localizedMessage)
                })
            }
            bookObject.authors = authorsToAdd
        }
            .setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, id ->
                    dialog.dismiss()
            })

        builder.create().show()
    }

    override fun onStop() {
        super.onStop()
        realmClass.close()
    }
}
