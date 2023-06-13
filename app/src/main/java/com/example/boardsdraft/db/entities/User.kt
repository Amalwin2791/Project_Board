package com.example.boardsdraft.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(

    var userName : String,
    var email : String,
    var password : String,
    var image : ByteArray?,
    var department: String?,
    var designation:String?,
    @PrimaryKey(autoGenerate = true)
    val userID : Int =0
)
