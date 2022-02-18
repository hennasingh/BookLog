package com.geek.booklog.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Author(

    @PrimaryKey
    var _id: ObjectId? = null,
    var name: String? = null,
    var books: RealmList<Book>? = null
): RealmObject() {
}