import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.socket.client.service.AddressService;
import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.service.Message;
import ro.ubb.socket.common.service.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AddressServiceTest {

    private AddressService addressService;
    private ExecutorService ex;
    private TcpClient tcp;

    @BeforeEach
    public void setUp() {
        this.ex = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        this.tcp = new TcpClient(Service.SERVER_HOST, Service.SERVER_PORT);
        this.addressService = new AddressService(this.ex, this.tcp);
    }

    @Test
    public void testAdd() {
        Address address = new Address("s", "ss", 1, "v");
        address.setId(1);
        assertDoesNotThrow(() -> this.addressService.add(address));
    }

    @Test
    public void testGet() {
        Message request = new Message(AddressService.GET_ADDRESSES, "aaa");
        Message response = tcp.sendAndReceive(request);
        List<Address> result = (List<Address>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.addressService.get());
    }

    @Test
    public void testDelete() {
        Message request = new Message(AddressService.DELETE_ADDRESS, 1);
        Message response = tcp.sendAndReceive(request);
        String optionalObj = (String) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> optionalObj, this.ex), this.addressService.delete(1));
    }

    @Test
    public void testUpdate()
    {
        Address address = new Address("s", "ss", 1, "v");
        address.setId(1);
        Message request = new Message(AddressService.DELETE_ADDRESS, 1);
        Message response = tcp.sendAndReceive(request);
        String optionalObj = (String) response.getBody();
        assertNotEquals(CompletableFuture.supplyAsync(() -> optionalObj, this.ex), this.addressService.update(address));

    }

    @Test
    public void testSort() {
        Message request = new Message(AddressService.SORT_ADDRESS, "sort");
        Message response = tcp.sendAndReceive(request);
        List<Address> result = (List<Address>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.addressService.sort());
    }

    @Test
    public void testFilter() {
        Message request = new Message(AddressService.FILTER_ADDRESS, "c1");
        Message response = tcp.sendAndReceive(request);
        Set<Address> result = (Set<Address>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.addressService.filter("c1"));
    }
}
