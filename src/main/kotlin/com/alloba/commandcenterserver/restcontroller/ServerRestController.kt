package com.alloba.commandcenterserver.restcontroller

import com.alloba.commandcenterserver.models.CommandList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("server")
class ServerRestController {
    var registeredClients: MutableList<String> = mutableListOf()

    @GetMapping("registerClient/{clientLocation}")
    fun registerClientEndpoint(@PathVariable("clientLocation") clientLocation: String): String {
        return if (registeredClients.contains(clientLocation))
            "Client $clientLocation Already Registered"
        else {
            registeredClients.add(clientLocation)
            "Client $clientLocation Registered"
        }
    }

    @GetMapping("executeRemoteCommand/{client}/{commandName}")
    fun executeRemoteCommandEndpoint(@PathVariable("client") client:String,
                                     @PathVariable("commandName") command:String) : String{
        val url = "http://$client/client/execute/$command"
        val restTemplate = RestTemplate()
        val ret = restTemplate.getForObject(url, String::class.java) //eventually might want to do something with a return value?

        return "Command Executed"
    }

    @GetMapping("removeClient/{client}")
    fun removeClientEndpoint(@PathVariable("client") client:String) : String{
        return if (registeredClients.contains(client)){
            registeredClients.remove(client)
            "Client $client Removed"
        }
        else{
            "Client $client Not In List"
        }
    }

    @GetMapping("listAllClients")
    fun listAllClients(): List<String> {
        pingClients()
        return registeredClients
    }

    fun pingClients(){
        registeredClients.forEach{
            try{
                getClientCommands(it)
            }
            catch (e:RuntimeException){
                registeredClients.remove(it)
            }
        }
    }

    fun getClientCommands(client:String): CommandList {
        return try {
            val url = "http://$client/client/commandList"
            val restTemplate = RestTemplate()
            val ret = restTemplate.getForObject(url, CommandList::class.java)
            ret ?: CommandList(emptyList())
        }
        catch (e:RuntimeException){
            CommandList(emptyList())
        }
    }
}
