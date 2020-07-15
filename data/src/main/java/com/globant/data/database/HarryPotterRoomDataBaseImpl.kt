package com.globant.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globant.data.entity.HouseRoom
import com.globant.data.mapper.SpellDataBaseMapper
import com.globant.domain.database.HarryPotterRoomDataBase
import com.globant.domain.entity.Spell
import com.globant.data.entity.SpellRoom
import com.globant.data.mapper.HouseDataBaseMapper
import com.globant.domain.entity.House
import com.globant.domain.util.Result
import java.lang.Exception

@Database(entities = [SpellRoom::class, HouseRoom::class], version = 1)
abstract class HarryPotterRoomDataBaseImpl : RoomDatabase(), HarryPotterRoomDataBase {

    abstract fun harryPotterDao(): HarryPotterRoomDao
    private val spellsMapper = SpellDataBaseMapper()
    private val houseMapper = HouseDataBaseMapper()

    override fun getSpellsFromDataBase(): Result<List<Spell>> =
        harryPotterDao().getSpells().let {
            if (it.isNotEmpty()) {
                Result.Success(spellsMapper.transformListOfSpells(it))
            } else {
                Result.Failure(Exception(SPELLS_ERROR))
            }
        }

    override fun updateSpells(spells: List<Spell>) {
        spells.forEach {
            harryPotterDao().insertSpell(spellsMapper.transformToData(it))
        }
    }

    override fun getHouseByName(name: String): Result<List<House>> =
        harryPotterDao().getHouseByName(name).let {
            if (it.isNotEmpty()) {
                Result.Success(houseMapper.transformToListOfHouse(it))
            } else {
                Result.Failure(Exception(HOUSE_ERROR))
            }
            // THIS METHOD WILL BE USED TO GET THE HOUSE FROM THE DATABASE BY A NAME PASSED
        }

    override fun updateHouses(houses: List<House>) {
        houses.forEach {
            harryPotterDao().insertHouse(houseMapper.transformToData(it))
        }
    }

    companion object {
        private const val SPELLS_ERROR = "Spells not found"
        private const val HOUSE_ERROR = "House not found"
    }
}
