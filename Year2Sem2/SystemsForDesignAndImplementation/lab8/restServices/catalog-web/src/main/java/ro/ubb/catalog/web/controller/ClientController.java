package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Client;
import ro.ubb.catalog.core.service.ServiceI;
import ro.ubb.catalog.web.converter.ClientConverter;
import ro.ubb.catalog.web.dto.ClientDto;
import ro.ubb.catalog.web.dto.ClientsDto;


import java.util.List;
import java.util.Optional;

@RestController
public class ClientController {

    private static final Logger Log = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    @Qualifier("clientService")
    private ServiceI<Integer, Client> clientService;

    @Autowired
    private ClientConverter clientConverter;

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    ClientsDto getAllClients() {
        Log.trace("getAllClients --- method entered");
        List<Client> clients = clientService.get();
        List<ClientDto> clientDtos = clientConverter.convertModelsToDtos(clients);
        ClientsDto result = new ClientsDto(clientDtos);
        Log.trace("getAllClients: result={}", result);
        return result;
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    ClientDto saveClient(@RequestBody ClientDto clientDto) {
        Log.trace("saveClient: dto={}", clientDto);
        Optional<Client> op = this.clientService.add(clientConverter.convertDtoToModel(clientDto));
        try {
            op.orElseThrow(Exception::new);
        } catch (Exception e) {
            return null;
        }
        ClientDto result = clientConverter.convertModelToDto(op.get());
        Log.trace("saveClient: result={}", result);
        return result;
    }

    @RequestMapping(value = "/clients/{id}", method = RequestMethod.PUT)
    ClientDto updateClient(@PathVariable Integer id, @RequestBody ClientDto clientDto) {
        Log.trace("updateClient: id={}, dto={}", id, clientDto);
        Optional<Client> op = clientService.update(clientConverter.convertDtoToModel(clientDto));
        ClientDto result = clientConverter.convertModelToDto(op.get());
        Log.trace("updateClient: result={}", result);
        return result;
    }

    @RequestMapping(value = "/clients/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteClient(@PathVariable Integer id) {
        Log.trace("deleteClient: id={}", id);
        clientService.delete(id);
        Log.trace("deleteClient --- method finished");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/clients/filter", method = RequestMethod.GET)
    ClientsDto filterFunction(@RequestParam String name) {
        Log.trace("filterClientAge --- method entered");
        List<Client> clients = clientService.filterFunction(name);
        List<ClientDto> clientDtos = clientConverter.convertModelsToDtos(clients);
        ClientsDto result = new ClientsDto(clientDtos);
        Log.trace("filterClientAge: result={}", result);
        return result;
    }

    @RequestMapping(value = "/clients/sort", method = RequestMethod.GET)
    ClientsDto sortFunction() {
        Log.trace("sortClients--- method entered");
        List<Client> clients = clientService.sortFunction();
        List<ClientDto> dtos = clientConverter.convertModelsToDtos(clients);
        ClientsDto result = new ClientsDto(dtos);
        Log.trace("sortClients: result={}", result);
        return result;
    }

    @RequestMapping(value = "/clients/sort2", method = RequestMethod.GET)
    ClientsDto sortMultipleFunction() {
        Log.trace("sortClients--- method entered");
        List<Client> clients = clientService.sortMultipleFunction();
        List<ClientDto> dtos = clientConverter.convertModelsToDtos(clients);
        ClientsDto result = new ClientsDto(dtos);
        Log.trace("sortClients: result={}", result);
        return result;
    }

    @RequestMapping(value = "/clients/sort3", method = RequestMethod.GET)
    ClientsDto sortMultipleFunctionDesc() {
        Log.trace("sortClients--- method entered");
        List<Client> clients = clientService.sortMultipleFunctionDesc();
        List<ClientDto> dtos = clientConverter.convertModelsToDtos(clients);
        ClientsDto result = new ClientsDto(dtos);
        Log.trace("sortClients: result={}", result);
        return result;
    }

}
