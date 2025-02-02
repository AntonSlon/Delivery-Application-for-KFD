package org.example.deliveryapplication.database.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.deliveryapplication.database.entity.User
import org.example.deliveryapplication.model.request.UserRegisterRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class TempStorage(
    val redisTemplate: RedisTemplate<String, User>,
    private val objectMapper: ObjectMapper
) {

    fun addAuthCode(uuid: String, user: User){
        println("Добавляем в редис uuid")
        redisTemplate.opsForValue().set(uuid, user)
    }

    fun getUser(uuid: String): User? {
        val userJson = redisTemplate.opsForValue().get(uuid)
        println(userJson)
        val user: User = objectMapper.convertValue(userJson, userJson!!::class.java)
        println("$user - user из getUser")
        return user
    }

    fun deleteAuthCode(email: String){
        redisTemplate.delete(email)
    }

}