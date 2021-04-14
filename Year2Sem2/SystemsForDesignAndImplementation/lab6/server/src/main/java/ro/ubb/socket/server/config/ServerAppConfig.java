package ro.ubb.socket.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import ro.ubb.socket.common.domain.*;
import ro.ubb.socket.common.domain.validators.*;
import ro.ubb.socket.common.service.*;
import ro.ubb.socket.server.repository.dbRepository.*;
import ro.ubb.socket.server.service.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ServerAppConfig {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Bean
    RmiServiceExporter rmiServiceExporterAddress() {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("AddressService");
        exporter.setServiceInterface(AddressService.class);
        exporter.setService(addressService());
        return exporter;
    }

    @Bean
    RmiServiceExporter rmiServiceExporterClient() {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("ClientService");
        exporter.setServiceInterface(ClientService.class);
        exporter.setService(clientService());
        return exporter;
    }

    @Bean
    RmiServiceExporter rmiServiceExporterDish() {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("DishService");
        exporter.setServiceInterface(DishService.class);
        exporter.setService(dishService());
        return exporter;
    }

    @Bean
    RmiServiceExporter rmiServiceExporterOrder() {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("OrderService");
        exporter.setServiceInterface(OrderService.class);
        exporter.setService(orderService());
        return exporter;
    }

    @Bean
    RmiServiceExporter rmiServiceExporterRestaurant() {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("RestaurantService");
        exporter.setServiceInterface(RestaurantService.class);
        exporter.setService(restaurantService());
        return exporter;
    }

    @Bean
    OrderDbRepo orderDbRepo() {
        Validator<Order> orderValidator = new OrderValidator();
        return new OrderDbRepo(orderValidator);
    }

    @Bean
    AddressDbRepo addressDbRepo() {
        Validator<Address> addressValidator = new AddressValidator();
        return new AddressDbRepo(addressValidator);
    }

    @Bean
    ClientDbRepo clientDbRepo() {
        Validator<Client> clientValidator = new ClientValidator();
        return new ClientDbRepo(clientValidator);
    }

    @Bean
    DishDbRepo dishDbRepo() {
        Validator<Dish> dishValidator = new DishValidator();
        return new DishDbRepo(dishValidator);
    }

    @Bean
    RestaurantDbRepo restaurantDbRepo() {
        Validator<Restaurant> restaurantValidator = new RestaurantValidator();
        return new RestaurantDbRepo(restaurantValidator);
    }

    @Bean
    AddressService addressService() {
        AddressDbRepo addressDbRepo = addressDbRepo();
        return new AddressServiceImpl(addressDbRepo, executorService);
    }

    @Bean
    ClientService clientService() {
        ClientDbRepo clientDbRepo = clientDbRepo();
        return new ClientServiceImpl(clientDbRepo, executorService);
    }

    @Bean
    DishService dishService() {
        DishDbRepo dishDbRepo = dishDbRepo();
        return new DishServiceImpl(dishDbRepo, executorService);
    }

    @Bean
    OrderService orderService() {
        OrderDbRepo orderDbRepo = orderDbRepo();
        return new OrderServiceImpl(orderDbRepo, executorService,clientDbRepo(),restaurantDbRepo());
    }

    @Bean
    RestaurantService restaurantService() {
        RestaurantDbRepo restaurantDbRepo = restaurantDbRepo();
        return new RestaurantServiceImpl(restaurantDbRepo, executorService);
    }
}
