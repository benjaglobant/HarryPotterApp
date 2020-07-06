package com.globant.data.service

import com.globant.data.mapper.SpellMapper
import com.globant.data.service.api.HarryPotterApi
import com.globant.domain.entity.Spell
import com.globant.domain.service.SpellsService
import com.globant.domain.util.Result

class SpellsServiceImpl : SpellsService {

    private val api = ServiceGenerator()
    private val mapper = SpellMapper()

    override fun getSpellsFromAPI(): Result<List<Spell>> {
        try {
            val callResponse = api.createService(HarryPotterApi::class.java).getSpells()
            val response = callResponse.execute()
            if (response.isSuccessful)
                response.body()?.let {
                    mapper.transformListOfSpells(it)
                }?.let {
                    return Result.Success(it)
                }
        } catch (e: Exception) {
            return Result.Failure(Exception(NOT_FOUND))
        }
        return Result.Failure(Exception(NOT_FOUND))
    }

    companion object {
        private const val NOT_FOUND = "Spells not found"
    }
}
