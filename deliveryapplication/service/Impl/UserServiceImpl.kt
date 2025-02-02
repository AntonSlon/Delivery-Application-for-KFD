package org.example.deliveryapplication.service.Impl

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.deliveryapplication.database.repository.UserDAO
import org.example.deliveryapplication.model.request.UserUpdateRequest
import org.example.deliveryapplication.model.response.UserUpdateResponse
import org.example.deliveryapplication.security.JwtFilter
import org.example.deliveryapplication.security.JwtParser
import org.example.deliveryapplication.service.UserService
import org.example.deliveryapplication.util.getPrincipal
//import org.example.deliveryapplication.util.getPrincipal
import org.springframework.stereotype.Service


@Service
class UserServiceImpl(private val userDAO: UserDAO,
                      private val jwtFilter: JwtFilter, private val jwtParser: JwtParser,
                      private val httpServletRequest: HttpServletRequest,
) : UserService {
    override fun update(request: UserUpdateRequest): UserUpdateResponse {
        val token = jwtParser.getTokenFromCookie(httpServletRequest)
        println("token from cookie: ${token}")
        println(jwtParser.parseToken(token))
        println(jwtParser.validateToken(token))
        val user = userDAO.findByEmail(jwtParser.extractEmail(token))

        if (request.name != user?.name)
            user?.name = request.name

        if (user?.email != request.email)
            user?.email = request.email

        if (user?.password != request.password)
            user?.password = request.password

        if (user?.telephoneNumber != request.telephoneNumber)
            user?.telephoneNumber = request.telephoneNumber

        println(user?.role)

        return UserUpdateResponse("Success")
    }
}