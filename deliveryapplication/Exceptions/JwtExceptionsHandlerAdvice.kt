package org.example.deliveryapplication.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

class TokenNotFoundException : Exception()

class InvalidTokenException : Exception()

@ControllerAdvice
class JwtExceptionsHandlerAdvice{
    @ExceptionHandler(TokenNotFoundException::class)
    fun handleTokenNotFoundException(): ResponseEntity<ExceptionDTO>{
        val exceptionDTO = ExceptionDTO("Token not found")
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionDTO)
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidTokenException(): ResponseEntity<ExceptionDTO>{
        val exceptionDTO = ExceptionDTO("Invalid token")
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionDTO)
    }
}