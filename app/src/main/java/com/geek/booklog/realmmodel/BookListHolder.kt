package com.geek.booklog.realmmodel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.booklist_item_layout.view.*

class BookListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val bookName = itemView.bookName
    private val authorName = itemView.authorName

    fun bindValues(book: BookRealm?){

    }
}