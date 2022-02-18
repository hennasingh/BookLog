package com.geek.booklog.model

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Book(
    @PrimaryKey
    var _id: ObjectId? = null,
    var name: String? = null,
    var isRead: Boolean = false,
    @LinkingObjects("books")
    var authors: RealmResults<Author>? = null
) : RealmObject() {
}