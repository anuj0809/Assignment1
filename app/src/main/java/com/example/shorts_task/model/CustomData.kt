package com.example.shorts_task.model

import java.io.Serializable

class CustomData(@Transient val posts : List<Post>) : Serializable