package com.example.shorts_task.model

import java.io.Serializable

data class Reaction(
    val count: Int,
    val voted: Boolean
) : Serializable
