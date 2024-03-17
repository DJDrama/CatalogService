package com.kotlinspring.controller

import io.github.oshai.kotlinlogging.*
import com.kotlinspring.service.GreetingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/greetings")
class GreetingController(
    private val greetingsService: GreetingService
) {

    private val logger = KotlinLogging.logger {  }
    @GetMapping("/{name}")
    fun retrieveGreeting(@PathVariable("name") name: String): String {
        logger.info { "hello world $name" }
        return greetingsService.retrieveGreeting(name = name)
    }


}