package io.waketunes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TimelineCommandServiceApplication

fun main(args: Array<String>) {
    runApplication<TimelineCommandServiceApplication>(*args)
}
