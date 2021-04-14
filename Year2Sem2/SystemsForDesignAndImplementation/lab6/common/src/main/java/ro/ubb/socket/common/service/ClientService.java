package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.Client;

import java.util.List;

public interface ClientService extends Service<Integer, Client> {

    /**
     * The function sorts the clients by their age.
     *
     * @return the clients sorted after their age
     */
    List<Client> sortClientAge();

    /**
     * The function filters the clients by an age.
     *
     * @param age - an age
     * @return the clients filtered after an age
     */
    List<Client> filterClientAge(int age);
}
