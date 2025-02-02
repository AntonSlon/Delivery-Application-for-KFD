package org.example.deliveryapplication.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

class UserExistException: Exception()

class IncorrectAuthCode: Exception()

class UserDoesntExistException: Exception()

class IncorrectPasswordException: Exception()

@ControllerAdvice
class AuthExceptionHandlerAdvice{
    @ExceptionHandler(UserExistException::class)
    fun handleAuthException(): ResponseEntity<ExceptionDTO>{
        val exceptionDTO = ExceptionDTO("User already exists")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<List<ExceptionDTO>>{
        val errors = ex.bindingResult.allErrors.map { error -> ExceptionDTO("${error.defaultMessage}") }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }

    @ExceptionHandler(IncorrectAuthCode::class)
    fun handleAccessDeniedException(): ResponseEntity<ExceptionDTO>{
        val exceptionDTO = ExceptionDTO("Access denied")
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionDTO)
    }

    @ExceptionHandler(UserDoesntExistException::class)
    fun handleUserDoesntExistException(): ResponseEntity<ExceptionDTO> {
        val exceptionDTO = ExceptionDTO("User does not exist")
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionDTO)
    }

    @ExceptionHandler(IncorrectPasswordException::class)
    fun handleInvalidPasswordException(): ResponseEntity<ExceptionDTO> {
        val exceptionDTO = ExceptionDTO("Password is invalid")
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionDTO)
    }
}
