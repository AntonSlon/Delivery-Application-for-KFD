package org.example.deliveryapplication.util

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

fun getPrincipal(): Long = SecurityContextHolder.getContext().authentication.principal as Long

