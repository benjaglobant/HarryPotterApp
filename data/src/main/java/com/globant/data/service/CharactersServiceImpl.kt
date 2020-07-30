package com.globant.data.service

import com.globant.data.mapper.CharacterMapper
import com.globant.data.service.api.HarryPotterApi
import com.globant.domain.entity.Character
import com.globant.domain.service.CharactersService
import com.globant.domain.util.Result

class CharactersServiceImpl : CharactersService {

    private val api = ServiceGenerator()
    private val mapper = CharacterMapper()

    override fun getCharactersByHouse(house: String): Result<List<Character>> {
        try {
            val callResponse = api.createService(HarryPotterApi::class.java).getCharactersByHouseName(house)
            val response = callResponse.execute()
            if (response.isSuccessful)
                response.body()?.let {
                    mapper.transformListOfCharacters(it)
                }?.let {
                    return Result.Success(it)
                }
        } catch (e: Exception) {
            return Result.Failure(Exception(NOT_FOUND))
        }
        return Result.Failure(Exception(NOT_FOUND))
    }

    companion object {
        private const val NOT_FOUND = "Characters not found"
    }
}
