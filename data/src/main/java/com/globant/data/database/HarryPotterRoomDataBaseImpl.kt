package com.globant.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globant.data.mapper.SpellDataBaseMapper
import com.globant.domain.database.HarryPotterRoomDataBase
import com.globant.domain.entity.Spell
import com.globant.data.entity.SpellRoom
import com.globant.domain.util.Result
import java.lang.Exception

@Database(entities = [SpellRoom::class], version = 1)
abstract class HarryPotterRoomDataBaseImpl : RoomDatabase(), HarryPotterRoomDataBase {

    abstract fun harryPotterDao(): HarryPotterRoomDao
    private val spellsMapper = SpellDataBaseMapper()

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

    companion object {
        private const val SPELLS_ERROR = "Spells not found"
    }
}
