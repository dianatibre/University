package ro.ubb.socket.client.service;

import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.Message;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class AddressService extends ServiceClientGeneric<Integer, Address>implements ro.ubb.socket.common.service.AddressService {
    public AddressService(ExecutorService ex, TcpClient tc){
        this.executorService=ex;
        this.tcpClient=tc;
    }

    @Override
    public CompletableFuture<Optional<Address>> add(Address address) throws ValidatorException { ;
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(AddressService.ADD_ADDRESS,address);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Address> result=null;
            if(response.getHeader().equals(Message.OK)) {
                OptionalObj<Address> optionalObj = (OptionalObj<Address>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            }
            else{
                String err =(String)response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Address>> get() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(AddressService.GET_ADDRESSES, "aaa");
            Message response = tcpClient.sendAndReceive(request);
            List<Address> result = (List<Address>)response.getBody();
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<Address>> delete(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(AddressService.DELETE_ADDRESS, id);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Address> result = null;
            if(response.getHeader().equals(Message.OK)) {
                OptionalObj<Address> optionalObj = (OptionalObj<Address>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            }
            else{
                String err =(String)response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<Address>> update(Address t) throws ValidatorException {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(AddressService.UPDATE_ADDRESS, t);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Address> result = null;
            if(response.getHeader().equals(Message.OK)) {
                OptionalObj<Address> optionalObj = (OptionalObj<Address>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            }
            else{
                String err =(String)response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Address>> sort() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(AddressService.SORT_ADDRESS,"sort");
            Message response = tcpClient.sendAndReceive(request);
            List<Address> result = (List<Address>)response.getBody();
            return result;
        }, executorService);
    }

    public CompletableFuture<Set<Address>> filter(String city) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(AddressService.FILTER_ADDRESS,city);
            Message response = tcpClient.sendAndReceive(request);
            Set<Address> result = (Set<Address>)response.getBody();
            return result;
        }, executorService);
    }


}
