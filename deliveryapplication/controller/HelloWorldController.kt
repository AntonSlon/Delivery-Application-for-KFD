package org.example.deliveryapplication.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/test")
class HelloWorldController{
    @GetMapping
    fun test(): String = "Hello World"
}