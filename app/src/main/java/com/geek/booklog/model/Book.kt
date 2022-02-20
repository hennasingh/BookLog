package com.geek.booklog.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class Book(
    @PrimaryKey
    var _id: ObjectId? = null,

    @Required
    var name: String = "",

    var isRead: Boolean = false,

    var authors: RealmList<Author> = RealmList()
) : RealmObject() {}