package com.globant.data.service

import com.globant.data.mapper.CharacterDetailMapper
import com.globant.data.service.api.HarryPotterApi
import com.globant.domain.entity.CharacterDetail
import com.globant.domain.service.CharacterDetailService
import com.globant.domain.util.Result

class CharacterDetailServiceImpl : CharacterDetailService {

    private val api = ServiceGenerator()
    private val mapper = CharacterDetailMapper()

    override fun getCharacterDetail(characterId: String): Result<CharacterDetail> {
        try {
            val callResponse = api.createService(HarryPotterApi::class.java).getCharacterDetailByCharacterId(characterId)
            val response = callResponse.execute()
            if (response.isSuccessful)
                response.body()?.let {
                    mapper.transform(it)
                }?.let {
                    return Result.Success(it)
                }
        } catch (e: Exception) {
            return Result.Failure(Exception(NOT_FOUND))
        }
        return Result.Failure(Exception(NOT_FOUND))
    }

    companion object {
        private const val NOT_FOUND = "Character Details not found"
    }
}
