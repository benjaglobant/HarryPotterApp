package com.globant.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globant.data.entity.CharacterEntity
import com.globant.data.entity.HouseDetailEntity
import com.globant.data.entity.HouseRoom
import com.globant.data.mapper.SpellDataBaseMapper
import com.globant.domain.database.HarryPotterDataBase
import com.globant.domain.entity.Spell
import com.globant.data.entity.SpellRoom
import com.globant.data.mapper.CharacterDatabaseMapper
import com.globant.data.mapper.HouseDataBaseMapper
import com.globant.data.mapper.HouseDetailDatabaseMapper
import com.globant.domain.entity.Character
import com.globant.domain.entity.House
import com.globant.domain.entity.HouseDetail
import com.globant.domain.util.Result
import java.lang.Exception

@Database(entities = [SpellRoom::class, HouseRoom::class, HouseDetailEntity::class, CharacterEntity::class], version = 1)
abstract class HarryPotterDataBaseImpl : RoomDatabase(), HarryPotterDataBase {

    abstract fun harryPotterDao(): HarryPotterDao
    private val spellsMapper = SpellDataBaseMapper()
    private val houseMapper = HouseDataBaseMapper()
    private val houseDetailMapper = HouseDetailDatabaseMapper()
    private val characterMapper = CharacterDatabaseMapper()

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
        }

    override fun getHouses(): Result<List<House>> =
        harryPotterDao().getHouses().let {
            if (it.isNotEmpty()) {
                Result.Success(houseMapper.transformToListOfHouse(it))
            } else {
                Result.Failure(Exception(HOUSES_ERROR))
            }
        }

    override fun updateHouses(houses: List<House>) {
        houses.forEach {
            harryPotterDao().insertHouse(houseMapper.transformToData(it))
        }
    }

    override fun getHouseDetailByName(houseName: String): Result<List<HouseDetail>> =
        harryPotterDao().getHouseDetailByName(houseName).let {
            if (it.isNotEmpty()) {
                Result.Success(houseDetailMapper.transformToListOfHouseDetail(it))
            } else {
                Result.Failure(Exception(HOUSE_DETAIL_ERROR))
            }
        }

    override fun updateHouseDetail(housesDetail: List<HouseDetail>) {
        housesDetail.forEach {
            harryPotterDao().insertHouseDetail(houseDetailMapper.transformToData(it))
        }
    }

    override fun getCharactersByHouseName(houseName: String): Result<List<Character>> =
        harryPotterDao().getCharactersByHouseName(houseName).let {
            if (it.isNotEmpty()) {
                Result.Success(characterMapper.transformToListOfCharacter(it))
            } else {
                Result.Failure(Exception(CHARACTERS_ERROR))
            }
        }

    override fun updateCharacters(characters: List<Character>, houseName: String) {
        characters.forEach {
            harryPotterDao().insertCharacter(characterMapper.transformToData(it, houseName))
        }
    }

    companion object {
        private const val SPELLS_ERROR = "Spells not found"
        private const val HOUSE_ERROR = "House not found"
        private const val HOUSES_ERROR = "Houses not found"
        private const val HOUSE_DETAIL_ERROR = "House detail not found"
        private const val CHARACTERS_ERROR = "Characters not found"
    }
}
