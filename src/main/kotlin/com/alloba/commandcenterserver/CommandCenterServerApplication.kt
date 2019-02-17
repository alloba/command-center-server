package com.alloba.commandcenterserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CommandCenterServerApplication

fun main(args: Array<String>) {
	runApplication<CommandCenterServerApplication>(*args)
}

