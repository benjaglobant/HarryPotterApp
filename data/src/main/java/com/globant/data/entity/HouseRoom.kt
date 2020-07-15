package com.globant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "house_table")
class HouseRoom(
    @PrimaryKey val name: String,
    val id: String
)
