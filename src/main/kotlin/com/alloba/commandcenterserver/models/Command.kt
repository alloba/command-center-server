package com.alloba.commandcenterserver.models

data class Command(val name: String="", val description:String="", val exec: String="", val type:String="")
data class CommandList(val commandList: List<Command>)