package com.pkpanda.emojistatus

import com.google.type.Date

data class User(
        val displayName: String = "",
        val emojis: String = "",
        val photoURL : String ="",
        val updatedAt : Long?= null,
)
