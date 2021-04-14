import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.socket.client.service.RestaurantService;
import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.service.Message;
import ro.ubb.socket.common.service.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RestaurantServiceTest {

    private RestaurantService restaurantService;
    private ExecutorService ex;
    private TcpClient tcp;

    @BeforeEach
    public void setUp() {
        this.ex = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        this.tcp = new TcpClient(Service.SERVER_HOST, Service.SERVER_PORT);
        this.restaurantService = new RestaurantService(this.ex, this.tcp);
    }

    @Test
    public void testAdd() {
        Restaurant restaurant = new Restaurant("name", 20, 1, true);
        restaurant.setId(1);
        assertDoesNotThrow(() -> this.restaurantService.add(restaurant));
    }

    @Test
    public void testGet() {
        Message request = new Message(RestaurantService.GET_RESTAURANT, "aaa");
        Message response = tcp.sendAndReceive(request);
        List<Restaurant> result = (List<Restaurant>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.restaurantService.get());
    }

    @Test
    public void testDelete() {
        Message request = new Message(RestaurantService.DELETE_RESTAURANT, 1);
        Message response = tcp.sendAndReceive(request);
        String optionalObj = (String) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> optionalObj, this.ex), this.restaurantService.delete(1));
    }

    @Test
    public void testUpdate() {
        Restaurant restaurant = new Restaurant("name", 20, 1, false);
        restaurant.setId(1);
        Message request = new Message(RestaurantService.DELETE_RESTAURANT, 1);
        Message response = tcp.sendAndReceive(request);
        String optionalObj = (String) response.getBody();
        assertNotEquals(CompletableFuture.supplyAsync(() -> optionalObj, this.ex), this.restaurantService.update(restaurant));

    }

    @Test
    public void testSort() {
        Message request = new Message(RestaurantService.SORT_RESTAURANT, "sort");
        Message response = tcp.sendAndReceive(request);
        List<Restaurant> result = (List<Restaurant>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.restaurantService.sort());
    }

    @Test
    public void testFilter() {
        Message request = new Message(RestaurantService.FILTER_RESTAURANT, 20);
        Message response = tcp.sendAndReceive(request);
        Set<Restaurant> result = (Set<Restaurant>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.restaurantService.filter(20));
    }
}
