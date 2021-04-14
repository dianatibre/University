package ro.ubb.socket.client.service;

import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Dish;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.Message;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class DishService extends ServiceClientGeneric<Integer, Dish> implements ro.ubb.socket.common.service.DishService {

    public DishService(ExecutorService ex, TcpClient tc){
        this.executorService=ex;
        this.tcpClient=tc;
    }

    @Override
    public CompletableFuture<Optional<Dish>> add (Dish dish) throws ValidatorException {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(DishService.ADD_DISH, dish);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Dish> result = null;
            if (response.getHeader().equals(Message.OK)) {
                OptionalObj<Dish> optionalObj = (OptionalObj<Dish>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            }
            else {
                String err = (String) response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Dish>> get() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(DishService.GET_DISHES, "aaa");
            Message response = tcpClient.sendAndReceive(request);
            List<Dish> result = (List<Dish>)response.getBody();
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<Dish>> delete(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(DishService.DELETE_DISH, id);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Dish> result = null;
            if(response.getHeader().equals(Message.OK)) {
                OptionalObj<Dish> optionalObj = (OptionalObj<Dish>) response.getBody();
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
    public CompletableFuture<Optional<Dish>> update(Dish t) throws ValidatorException {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(DishService.UPDATE_DISH, t);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Dish> result = null;
            if(response.getHeader().equals(Message.OK)) {
                OptionalObj<Dish> optionalObj = (OptionalObj<Dish>) response.getBody();
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
    public CompletableFuture<List<Dish>> sort() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(DishService.SORT_DISH,"sort");
            Message response = tcpClient.sendAndReceive(request);
            List<Dish> result = (List<Dish>)response.getBody();
            return result;
        }, executorService);
    }

    public CompletableFuture<Set<Dish>> filter(String category) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(DishService.FILTER_DISH,category);
            Message response = tcpClient.sendAndReceive(request);
            Set<Dish> result = (Set<Dish>)response.getBody();
            return result;
        }, executorService);
    }
}
