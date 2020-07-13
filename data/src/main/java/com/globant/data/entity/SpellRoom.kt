package com.globant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spells_table")
class SpellRoom(
    @PrimaryKey val spell: String,
    val type: String,
    val effect: String
)
