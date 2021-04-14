package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.Client;

public interface ClientService extends Service<Integer, Client> {

    String ADD_CLIENT="addClient";
    String GET_CLIENTS="getClients";
    String DELETE_CLIENTS="deleteClient";
    String UPDATE_CLIENT="updateClient";
    String SORT_CLIENT="sortClient";
    String FILTER_CLIENT="filterClient";
}
