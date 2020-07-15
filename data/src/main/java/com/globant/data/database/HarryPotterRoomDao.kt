package com.globant.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.data.entity.HouseRoom
import com.globant.data.entity.SpellRoom

@Dao
interface HarryPotterRoomDao {
    @Query("SELECT * FROM spells_table")
    fun getSpells(): List<SpellRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSpell(spell: SpellRoom)

    @Query("SELECT * FROM house_table WHERE name = :name")
    fun getHouseByName(name: String): List<HouseRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHouse(house: HouseRoom)
}
