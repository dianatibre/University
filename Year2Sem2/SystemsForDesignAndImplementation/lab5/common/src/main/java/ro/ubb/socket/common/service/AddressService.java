package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.Address;

public interface AddressService extends Service<Integer, Address>{
    String ADD_ADDRESS="addAddress";
    String GET_ADDRESSES="getAddresses";
    String DELETE_ADDRESS="deleteAddress";
    String UPDATE_ADDRESS="updateAddress";
    String SORT_ADDRESS="sortAddress";
    String FILTER_ADDRESS="filterAddress";
}
