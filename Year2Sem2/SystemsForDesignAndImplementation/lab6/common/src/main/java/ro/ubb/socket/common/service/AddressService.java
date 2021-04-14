package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.Address;

import java.util.List;

public interface AddressService extends Service<Integer, Address> {
    /**
     * The function sorts the addresses by their city.
     *
     * @return the addresses sorted after their city
     */
    List<Address> sortAddressCity();

    /**
     * The function filters the addresses by a city.
     *
     * @param city - a city
     * @return the addresses filtered after a city
     */
    List<Address> filterAddressCity(String city);
}
