package org.example.deliveryapplication.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.deliveryapplication.Exceptions.TokenNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis
import java.util.*
import javax.crypto.spec.SecretKeySpec

const val EXPIRATION_TIME: Long = 86_400_000

@Service
class JwtParser(
    @Value("\${jwt.secret}") private val secret: String = ""
){
    private val signingKey: SecretKeySpec
        get() {
            val keyByte: ByteArray = Base64.getDecoder().decode(secret)
            return SecretKeySpec(keyByte, 0, keyByte.size, "HmacSHA256")
        }

    fun parseToken(token: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).body
        }catch (e: ExpiredJwtException){
            throw Exception("Expired JWT Token")
        }catch (e: SignatureException){
            throw Exception("Invalid JWT Token")
        }catch (e: MalformedJwtException){
            throw Exception("Malformed JWT Token") }
    }

    fun validateToken(token: String): Boolean{
        return try {
            val claims = parseToken(token)
            !claims.expiration.before(Date())
            true
        }catch (e: Exception){
            throw Exception("Ошибка валидации токена")
        }
    }

    fun generateJwt(email: String): String {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(currentTimeMillis() + EXPIRATION_TIME))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractEmail(token: String): String =
        parseToken(token).subject

    fun setTokenInCookie(response: HttpServletResponse, token: String) {
        val cookie = Cookie("jwt", token)
        cookie.domain = "localhost"
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.path = "/"
        cookie.maxAge = 3600
        response.addCookie(cookie)
    }

    fun getTokenFromCookie(request: HttpServletRequest): String {
        val header = request.getHeader("authorization")
        return if (header != null && header.startsWith("Bearer ")){
            header.substring(7)
        }else throw TokenNotFoundException()
    }

}