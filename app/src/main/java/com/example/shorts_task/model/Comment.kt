package com.example.shorts_task.model

import java.io.Serializable

data class Comment(
    val count: Int,
    val commentingAllowed: Boolean
) : Serializable
