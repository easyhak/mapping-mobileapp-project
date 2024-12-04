package com.trip.myapp.domain.model

data class User(
    val uid: String,
    val name: String,
    val posts: MutableList<Post> = mutableListOf(),
    val archives: MutableList<Archive> = mutableListOf()
)
