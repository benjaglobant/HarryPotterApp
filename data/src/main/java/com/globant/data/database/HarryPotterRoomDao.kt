package com.globant.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.data.entity.SpellRoom

@Dao
interface HarryPotterRoomDao {
    @Query("SELECT * FROM spells_table")
    fun getSpells(): List<SpellRoom>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSpell(spell: SpellRoom)
}
