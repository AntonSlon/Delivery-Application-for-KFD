package org.example.deliveryapplication.Exceptions

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component


data class ExceptionDTO(
    val exceptionDescription: String,
)

