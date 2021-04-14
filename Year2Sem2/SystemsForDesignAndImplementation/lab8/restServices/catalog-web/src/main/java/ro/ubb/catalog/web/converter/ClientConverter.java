package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Address;
import ro.ubb.catalog.core.model.Client;
import ro.ubb.catalog.web.dto.AddressDto;
import ro.ubb.catalog.web.dto.ClientDto;

@Component
public class ClientConverter extends BaseConverter<Client, ClientDto> {

    @Override
    public Client convertDtoToModel(ClientDto dto) {
        Client client = Client.builder().name(dto.getName()).age(dto.getAge()).address(dto.getAddress()).email(dto.getEmail()).build();
        client.setId(dto.getId());
        return client;
    }

    @Override
    public ClientDto convertModelToDto(Client client) {
        ClientDto clientDto = ClientDto.builder().name(client.getName()).age(client.getAge()).email(client.getEmail()).address(client.getAddress()).build();
        clientDto.setId(client.getId());
        return clientDto;
    }
}
