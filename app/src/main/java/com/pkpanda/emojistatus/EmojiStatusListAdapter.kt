package com.pkpanda.emojistatus

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class EmojiStatusListAdapter(private val context: Context, options: FirestoreRecyclerOptions<User>) : FirestoreRecyclerAdapter<User, UserViewHolder>(options){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.emoji_status_list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        val tvName : TextView = holder.itemView.findViewById(R.id.displayName)
        val tvEmojis : TextView = holder.itemView.findViewById(R.id.emojis)
        tvName.text = model.displayName
        tvEmojis.text = model.emojis
    }
}
