import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.socket.client.service.DishService;
import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Dish;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.service.Message;
import ro.ubb.socket.common.service.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DishServiceTest {

    private DishService dishService;
    private ExecutorService ex;
    private TcpClient tcp;

    @BeforeEach
    public void setUp() {
        this.ex = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        this.tcp = new TcpClient(Service.SERVER_HOST, Service.SERVER_PORT);
        this.dishService = new DishService(this.ex, this.tcp);
    }

    @Test
    public void testAdd() {
        Dish dish = new Dish("s", 2.1f, 1, "2");
        dish.setId(1);
        assertDoesNotThrow(() -> this.dishService.add(dish));
    }

    @Test
    public void testGet() {
        Message request = new Message(DishService.GET_DISHES, "aaa");
        Message response = tcp.sendAndReceive(request);
        List<Client> result = (List<Client>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.dishService.get());
    }

    @Test
    public void testDelete() {
        Message request = new Message(DishService.DELETE_DISH, 1);
        Message response = tcp.sendAndReceive(request);
        OptionalObj<Dish> optionalObj = (OptionalObj<Dish>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> optionalObj, this.ex), this.dishService.delete(1));
    }

    @Test
    public void testUpdate() {
        Dish dish = new Dish("s", 2.1f, 1, "2");
        dish.setId(1);
        Message request = new Message(DishService.DELETE_DISH, 1);
        Message response = tcp.sendAndReceive(request);
        String optionalObj = (String) response.getBody();
        assertNotEquals(CompletableFuture.supplyAsync(() -> optionalObj, this.ex), this.dishService.update(dish));

    }

    @Test
    public void testSort() {
        Message request = new Message(DishService.SORT_DISH, "sort");
        Message response = tcp.sendAndReceive(request);
        List<Dish> result = (List<Dish>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.dishService.sort());
    }

    @Test
    public void testFilter() {
        Message request = new Message(DishService.FILTER_DISH, "c1");
        Message response = tcp.sendAndReceive(request);
        Set<Dish> result = (Set<Dish>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.dishService.filter("c1"));
    }
}
