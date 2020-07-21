package com.globant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "house_detail_table")
class HouseDetailEntity(
    @PrimaryKey val name: String,
    val mascot: String,
    val director: String,
    val ghost: String,
    val founder: String
)
