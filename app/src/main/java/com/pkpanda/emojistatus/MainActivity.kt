package com.pkpanda.emojistatus

import android.R.attr.*
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
class MainActivity : AppCompatActivity() {
    private companion object{
        private const val TAG = "MainActivity"
        private val VALID_CHAR_TYPES = listOf(
                Character.NON_SPACING_MARK, // 6
                Character.DECIMAL_DIGIT_NUMBER, // 9
                Character.LETTER_NUMBER, // 10
                Character.OTHER_NUMBER, // 11
                Character.SPACE_SEPARATOR, // 12
                Character.FORMAT, // 16
                Character.SURROGATE, // 19
                Character.DASH_PUNCTUATION, // 20
                Character.START_PUNCTUATION, // 21
                Character.END_PUNCTUATION, // 22
                Character.CONNECTOR_PUNCTUATION, // 23
                Character.OTHER_PUNCTUATION, // 24
                Character.MATH_SYMBOL, // 25
                Character.CURRENCY_SYMBOL, //26
                Character.MODIFIER_SYMBOL, // 27
                Character.OTHER_SYMBOL // 28
        ).map { it.toInt() }.toSet()
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var rvUsers : RecyclerView
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        rvUsers = findViewById(R.id.rvUsers)
        auth = Firebase.auth

        val query = db.collection("users")
        val options = FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java)
            .setLifecycleOwner(this).build()

        val adapter = EmojiStatusListAdapter(this, options)
        rvUsers.adapter = adapter
        rvUsers.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.miLogout){
            Log.i(TAG, "Logout")
            auth.signOut()
            val logoutIntent = Intent(this, LoginActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
        }else if(item.itemId == R.id.miEdit){
            Log.i(TAG, "Edit")
            showAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }
    inner class EmojiFilter : InputFilter {
        override fun filter(source: CharSequence?, p1: Int, p2: Int, p3: Spanned?, p4: Int, p5: Int): CharSequence {
            if (source == null || source.isBlank()) {
                return ""
            }
            val validCharTypes = listOf(Character.SURROGATE, Character.NON_SPACING_MARK, Character.OTHER_SYMBOL).map { it.toInt() }
            for (inputChar in source) {
                val type = Character.getType(inputChar)
                Log.i(TAG, "Character type $type")
                if (!validCharTypes.contains(type)) {
                    if (!VALID_CHAR_TYPES.contains(type)) {
                        Toast.makeText(this@MainActivity, "Only emojis are allowed", Toast.LENGTH_SHORT).show()
                        return ""
                    }
                }
            }
            return source
        }
    }
    private fun showAlertDialog() {
        val editStatus = EditText(this)
        val lengthFilter = InputFilter.LengthFilter(12)
        val emojiFilter = EmojiFilter()
        editStatus.filters = arrayOf(lengthFilter, emojiFilter)
        val dialog = AlertDialog.Builder(this)
                .setTitle("Update Emoji Status")
                .setMessage("What's on your mind")
                .setView(editStatus)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Update", null)
                .show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            Log.i(TAG, "Clicked on update")
            val emojisEntered = editStatus.text.toString()
            if(emojisEntered.isBlank()){
                Toast.makeText(this, "Cannot Set Empty Text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val currentUser = auth.currentUser ?: return@setOnClickListener
            db.collection("users").document(currentUser.uid).update("emojis", emojisEntered)
            dialog.dismiss()
        }
    }
}