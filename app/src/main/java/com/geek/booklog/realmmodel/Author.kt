package com.geek.booklog.realmmodel

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class Author(

    @PrimaryKey
    var _id: ObjectId = ObjectId(),

    @Required
    var name: String = "",

    @LinkingObjects("authors")
    val books: RealmResults<BookRealm>? = null
): RealmObject() { }