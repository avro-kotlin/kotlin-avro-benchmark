package com.github.avrokotlin.benchmark.data.model

import kotlinx.serialization.Serializable


@Serializable
class Users(
    var users: MutableList<User> = mutableListOf()
)
@Serializable
class User(
    var id: String? = null,
    var index: Int = 0,
    var guid: String? = null,
    var isActive: Boolean = false,
    var balance: String? = null,
    var picture: String? = null,
    var age: Int = 0,
    var eyeColor: String? = null,
    var name: String? = null,
    var gender: String? = null,
    var company: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var about: String? = null,
    var registered: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var tags: List<String> = emptyList(),
    var friends: List<Friend> = emptyList(),
    var greeting: String? = null,
    var favoriteFruit: String? = null
)
@Serializable
class Friend(
    val id: String? = null,
    val name: String? = null
)
