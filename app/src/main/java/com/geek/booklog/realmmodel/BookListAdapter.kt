package com.geek.booklog.realmmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import com.geek.booklog.R
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class BookListAdapter(books: OrderedRealmCollection<BookRealm>): RealmRecyclerViewAdapter<BookRealm, BookListHolder>(books, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.booklist_item_layout, parent, false)
        return BookListHolder(view)
    }

    override fun onBindViewHolder(holder: BookListHolder, position: Int) {
       val book = getItem(position)
        holder.bindValues(book)
    }


}