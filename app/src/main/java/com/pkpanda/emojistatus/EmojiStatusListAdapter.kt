package com.pkpanda.emojistatus

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class EmojiStatusListAdapter(private val context: Context, options: FirestoreRecyclerOptions<User>) : FirestoreRecyclerAdapter<User, UserViewHolder>(
    options
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.emoji_status_list_item,
            parent,
            false
        )
        return UserViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        val tvName : TextView = holder.itemView.findViewById(R.id.displayName)
        val tvEmojis : TextView = holder.itemView.findViewById(R.id.emojis)
        val tvTime : TextView = holder.itemView.findViewById(R.id.tvTime)
        val ivAvatar : ImageView = holder.itemView.findViewById(R.id.ivAvatar)

        val updatedAt = SimpleDateFormat("dd/MM HH:mm").format(model.updatedAt)
        tvName.text = model.displayName
        tvEmojis.text = model.emojis
        tvTime.text = updatedAt
        Glide.with(context).load(model.photoURL).into(ivAvatar);
    }
}
