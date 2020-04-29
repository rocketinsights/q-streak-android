package com.example.qstreak.network

import com.example.qstreak.models.User
import com.squareup.moshi.Json

data class CreateUserResponse(
    @field:Json(name = "uid") val uid: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "zip") val zip: String
) {
    fun toUserModel(): User {
        return User(zip, uid, name)
    }
}
