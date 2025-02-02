package org.example.deliveryapplication.service.Impl

import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletResponse
import org.example.deliveryapplication.Exceptions.*
import org.example.deliveryapplication.database.entity.User
import org.example.deliveryapplication.database.repository.TempStorage
import org.example.deliveryapplication.database.repository.UserDAO
import org.example.deliveryapplication.model.request.UserLoginRequest
import org.example.deliveryapplication.model.request.UserRegisterRequest
import org.example.deliveryapplication.model.response.AuthCodeResponse
import org.example.deliveryapplication.model.response.UserLoginResponse
import org.example.deliveryapplication.model.response.UserRegisterResponse
import org.example.deliveryapplication.security.JwtParser
import org.example.deliveryapplication.security.JwtUserDetailsService
import org.example.deliveryapplication.service.UserAuthService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

import org.springframework.stereotype.Service


@Service
class UserAuthServiceAuthImpl(
    private val userDAO: UserDAO,
    private val mailSenderServiceImpl: MailSenderServiceImpl,
    private val tempStorage: TempStorage,
    private val jwtParser: JwtParser,
    private val httpServletResponse: HttpServletResponse,
    //private val jwtUserDetailsService: JwtUserDetailsService
): UserAuthService{
    override fun register(request: UserRegisterRequest): UserRegisterResponse {
        val user = User(name = request.name, email = request.email,
            telephoneNumber = request.telephoneNumber, password = request.password)
        println(user)
        if (userDAO.existsByEmail(user.email))
            throw UserExistException()
        mailSenderServiceImpl.sendAuthorizationCode(user.email)
        println(request)
        tempStorage.addAuthCode(mailSenderServiceImpl.uuid, user)
        println(mailSenderServiceImpl.uuid)
        return UserRegisterResponse(name = user.name, email = user.email,
            telephoneNumber = user.telephoneNumber, id = user.id, status = "Pending")
    }

    override fun confirmRegister(uuid: String): AuthCodeResponse {
        println(uuid == mailSenderServiceImpl.uuid)
        if (tempStorage.getUser(uuid) != null)
            println("$uuid - uuid при подтверждении")
            println("${tempStorage.getUser(uuid)} - Данные пользователя")
            userDAO.save(tempStorage.getUser(uuid)!!)
        return AuthCodeResponse(status = "Success registration.")
    }

    override fun login(request: UserLoginRequest): UserLoginResponse {
        if (!userDAO.existsByEmail(request.email))
            throw UserDoesntExistException()
        if (userDAO.findByEmail(request.email)?.password != request.password)
            throw IncorrectPasswordException()
        val user = userDAO.findByEmail(request.email)
        user!!.role = request.role
        userDAO.save(user)
        println(user.role)
        val token = jwtParser.generateJwt(request.email)
        jwtParser.setTokenInCookie(httpServletResponse, token)
        println("token to cookie: $token")
        return UserLoginResponse(status = "200", "Success logged in")
    }

    override fun getAll(): List<User> = userDAO.findAll()

}