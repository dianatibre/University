import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.socket.client.service.ClientService;
import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.service.Message;
import ro.ubb.socket.common.service.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ClientServiceTest {

    private ClientService clientService;
    private ExecutorService ex;
    private TcpClient tcp;

    @BeforeEach
    public void setUp() {
        this.ex = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        this.tcp = new TcpClient(Service.SERVER_HOST, Service.SERVER_PORT);
        this.clientService = new ClientService(this.ex, this.tcp);
    }

    @Test
    public void testAdd() {
        Address address = new Address("s", "ss", 1, "v");
        address.setId(1);
        Client client = new Client("name", 20, 1, "str@yahoo");
        client.setId(1);
        assertDoesNotThrow(() -> this.clientService.add(client));
    }

    @Test
    public void testGet() {
        Message request = new Message(ClientService.GET_CLIENTS, "aaa");
        Message response = tcp.sendAndReceive(request);
        List<Client> result = (List<Client>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.clientService.get());
    }

    @Test
    public void testDelete() {
        Message request = new Message(ClientService.DELETE_CLIENTS, 1);
        Message response = tcp.sendAndReceive(request);
        String optionalObj = (String) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> optionalObj, this.ex), this.clientService.delete(1));
    }

    @Test
    public void testUpdate()
    {
        Address address = new Address("s", "ss", 1, "v");
        address.setId(1);
        Client client = new Client("name", 20, 1, "str@yahoo");
        client.setId(1);
        Message request = new Message(ClientService.DELETE_CLIENTS, 1);
        Message response = tcp.sendAndReceive(request);
        String optionalObj = (String) response.getBody();
        assertNotEquals(CompletableFuture.supplyAsync(() -> optionalObj, this.ex), this.clientService.update(client));

    }

    @Test
    public void testSort() {
        Message request = new Message(ClientService.SORT_CLIENT, "sort");
        Message response = tcp.sendAndReceive(request);
        List<Client> result = (List<Client>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.clientService.sort());
    }

    @Test
    public void testFilter() {
        Message request = new Message(ClientService.FILTER_CLIENT, 20);
        Message response = tcp.sendAndReceive(request);
        Set<Client> result = (Set<Client>) response.getBody();

        assertNotEquals(CompletableFuture.supplyAsync(() -> result, this.ex), this.clientService.filter(20));
    }
}
