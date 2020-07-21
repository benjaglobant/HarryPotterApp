package com.globant.domain.database

import com.globant.domain.entity.House
import com.globant.domain.entity.HouseDetail
import com.globant.domain.entity.Spell
import com.globant.domain.util.Result

interface HarryPotterDataBase {
    fun getSpellsFromDataBase(): Result<List<Spell>>
    fun updateSpells(spells: List<Spell>)
    fun getHouseByName(name: String): Result<List<House>>
    fun updateHouses(houses: List<House>)
    fun getHouses(): Result<List<House>>
    fun updateHouseDetail(housesDetail: List<HouseDetail>)
    fun getHouseDetailByName(houseName: String): Result<List<HouseDetail>>
}