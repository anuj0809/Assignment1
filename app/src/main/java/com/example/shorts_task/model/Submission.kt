package com.example.shorts_task.model

import java.io.Serializable

data class Submission(
    val title: String,
    val description: String,
    val mediaUrl: String,
    val thumbnail: String,
    val hyperlink: String,
    val placeholderUrl: String
) : Serializable