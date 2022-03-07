package com.geek.booklog.model

import com.geek.booklog.realmmodel.Author
import io.realm.RealmList
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

data class Book(
    var name: String ="",

    var isRead: Boolean = false,

    var authors: ArrayList<Author> = ArrayList()
) {
}