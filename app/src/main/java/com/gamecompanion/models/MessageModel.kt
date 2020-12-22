package com.gamecompanion

import java.util.Date


data class MessageModel (
    var text: String? = null,
    var createdAt: Date? = null,
    var userid: String? = null,
    var username: String? = null
)