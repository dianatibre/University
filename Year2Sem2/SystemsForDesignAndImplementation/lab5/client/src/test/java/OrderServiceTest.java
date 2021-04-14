import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.socket.client.service.OrderService;
import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.service.Message;
import ro.ubb.socket.common.service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OrderServiceTest {

    private OrderService dishService;
    private ExecutorService ex;
    private TcpClient tcp;

    @BeforeEach
    public void setUp() {
        this.ex = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        this.tcp = new TcpClient(Service.SERVER_HOST, Service.SERVER_PORT);
        this.dishService = new OrderService(this.ex, this.tcp);
    }

    @Test
    public void testAdd() {
        assertDoesNotThrow(() -> this.dishService.add(null));
    }

    @Test
    public void testGet() {
        Message request = new Message(OrderService.GET_ORDERS, "aaa");
        Message response = tcp.sendAndReceive(request);
        List<Client> result = (List<Client>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.dishService.get());
    }

    @Test
    public void testDelete() {
        Message request = new Message(OrderService.DELETE_ORDER, 1);
        Message response = tcp.sendAndReceive(request);
        String optionalObj = (String) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> optionalObj, this.ex), this.dishService.delete(1));
    }

    @Test
    public void testUpdate() {
        Message request = new Message(OrderService.DELETE_ORDER, 1);
        Message response = tcp.sendAndReceive(request);
        String optionalObj = (String) response.getBody();
        assertNotEquals(CompletableFuture.supplyAsync(() -> optionalObj, this.ex), this.dishService.update(null));

    }

    @Test
    public void testSort() {
        Message request = new Message(OrderService.SORT_ORDER, "sort");
        Message response = tcp.sendAndReceive(request);
        List<Order> result = (List<Order>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.dishService.sort());
    }

    @Test
    public void testFilter() {
        Message request = new Message(OrderService.FILTER_ORDER, "c1");
        Message response = tcp.sendAndReceive(request);
        Set<Order> result = (Set<Order>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.dishService.filter("c1"));
    }

    @Test
    public void testBestRestaurant() {
        Message request = new Message(OrderService.NUMBER_BEST_RESTAURANT, "aaa");
        Message response = tcp.sendAndReceive(request);
        String result = (String) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.dishService.bestRestaurants());
    }

    @Test
    public void testBestClient() {
        Message request = new Message(OrderService.MOST_DEVOTED_CLIENTS, "aaa");
        Message response = tcp.sendAndReceive(request);
        ArrayList<Order> result = (ArrayList<Order>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.dishService.mostDevotedClients());
    }
}

