package ro.ubb.socket.client.service;

import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.Message;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ClientService extends ServiceClientGeneric<Integer, Client> implements ro.ubb.socket.common.service.ClientService {

    public ClientService(ExecutorService ex, TcpClient tc) {
        this.executorService = ex;
        this.tcpClient = tc;
    }

    @Override
    public CompletableFuture<Optional<Client>> add(Client restaurant) throws ValidatorException {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(ro.ubb.socket.common.service.ClientService.ADD_CLIENT, restaurant);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Client> result;
            if (response.getHeader().equals(Message.OK)) {
                OptionalObj<Client> optionalObj = (OptionalObj<Client>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            } else {
                String err = (String) response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Client>> get() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(ro.ubb.socket.common.service.ClientService.GET_CLIENTS, "aaa");
            Message response = tcpClient.sendAndReceive(request);
            List<Client> result = (List<Client>) response.getBody();
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<Client>> delete(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(ro.ubb.socket.common.service.ClientService.DELETE_CLIENTS, id);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Client> result;
            if (response.getHeader().equals(Message.OK)) {
                OptionalObj<Client> optionalObj = (OptionalObj<Client>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            } else {
                String err = (String) response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<Client>> update(Client client) throws ValidatorException {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(ro.ubb.socket.common.service.ClientService.UPDATE_CLIENT, client);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Client> result;
            if (response.getHeader().equals(Message.OK)) {
                OptionalObj<Client> optionalObj = (OptionalObj<Client>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            } else {
                String err = (String) response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Client>> sort() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(ro.ubb.socket.common.service.ClientService.SORT_CLIENT, "sort");
            Message response = tcpClient.sendAndReceive(request);
            return (List<Client>) response.getBody();
        }, executorService);
    }

    public CompletableFuture<Set<Client>> filter(int age) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(ro.ubb.socket.common.service.ClientService.FILTER_CLIENT, age);
            Message response = tcpClient.sendAndReceive(request);
            return (Set<Client>) response.getBody();
        }, executorService);
    }
}
