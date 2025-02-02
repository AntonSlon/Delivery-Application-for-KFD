package org.example.deliveryapplication.model.request

import jakarta.validation.constraints.NotNull

data class AuthCodeRequest(
    @field:NotNull
    val code: Int
)