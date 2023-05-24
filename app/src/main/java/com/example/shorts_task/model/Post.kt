package com.example.shorts_task.model

import java.io.Serializable

data class Post(
    val postId: String,
    val creator: Creator,
    val comment: Comment,
    val reaction: Reaction,
    val submission: Submission
) :Serializable