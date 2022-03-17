package com.geek.booklog.realmmodel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.booklist_item_layout.view.*
import kotlin.text.StringBuilder

class BookListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val bookName = itemView.bookName
    private val authorName = itemView.authorName
    val stringBuilder = StringBuilder("")

    fun bindValues(book: BookRealm?){
        bookName.text = book?.name
        book?.authors?.forEach{
            stringBuilder.append(it.name).append(" ")
        }
        authorName.text = stringBuilder
    }
}