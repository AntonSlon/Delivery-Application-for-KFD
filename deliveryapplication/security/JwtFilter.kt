package org.example.deliveryapplication.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.deliveryapplication.Exceptions.InvalidTokenException
import org.example.deliveryapplication.Exceptions.TokenNotFoundException
import org.example.deliveryapplication.model.request.UserLoginRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.lang.System.currentTimeMillis
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtFilter(
    val jwtParser: JwtParser,
    val userDetails: JwtUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authorizationHeader: String? = request.getHeader("Authorization")

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                val token: String = request.getHeader("Authorization").substringAfter("Bearer ")
                val email = jwtParser.extractEmail(token)

                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails = userDetails.loadUserByUsername(email)

                    if (jwtParser.validateToken(token)) {
                        val authToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities)
                        SecurityContextHolder.getContext().authentication = authToken
                    }
                }

            }catch (ex: Exception){
                response.writer.write("FilterError")
            }
        }
        filterChain.doFilter(request, response)
    }

}