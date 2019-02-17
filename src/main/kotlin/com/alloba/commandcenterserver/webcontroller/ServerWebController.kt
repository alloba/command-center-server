package com.alloba.commandcenterserver.webcontroller

import com.alloba.commandcenterserver.models.CommandList
import com.alloba.commandcenterserver.restcontroller.ServerRestController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("server")
class ServerWebController{
    @Autowired
    lateinit var serverRestController : ServerRestController

    @Value("\${server.port:}")
    lateinit var port: String

    @GetMapping("")
    fun clientCommandsEndpoint(model: Model) : String{
        val clientList = serverRestController.listAllClients()
        val commandList = clientList.map{client -> serverRestController.getClientCommands(client)}

        val clientCommandZip: Map<String, CommandList> = clientList.zip(commandList).toMap()

        model.addAttribute("clientList", clientList)
        model.addAttribute("commandList", commandList)
        model.addAttribute("clientCommandZip", clientCommandZip)
        model.addAttribute("port", port)

        return "index"
    }
}