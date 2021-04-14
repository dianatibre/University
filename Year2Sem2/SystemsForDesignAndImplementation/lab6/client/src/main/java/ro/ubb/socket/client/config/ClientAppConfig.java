package ro.ubb.socket.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import ro.ubb.socket.client.service.*;
import ro.ubb.socket.client.ui.Console;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ClientAppConfig {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    //RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();

    @Bean
    Console console() {
        return new Console(new AddressService(executorService), new ClientService(executorService), new DishService(executorService), new OrderService(executorService), new RestaurantService(executorService));
    }

    @Bean
    AddressService addressService() {
        return new AddressService(executorService);
    }

    @Bean
    ClientService clientService() {
        return new ClientService((executorService));
    }

    @Bean
    DishService dishService() { return new DishService(executorService); }

    @Bean
    OrderService orderService() { return new OrderService(executorService); }

    @Bean
    RestaurantService restaurantService() { return new RestaurantService(executorService); }

    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanAddress() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(ro.ubb.socket.common.service.AddressService.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/AddressService");
        return rmiProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanClient() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(ro.ubb.socket.common.service.ClientService.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/ClientService");
        return rmiProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanDish() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(ro.ubb.socket.common.service.DishService.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/DishService");
        return rmiProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanOrder() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(ro.ubb.socket.common.service.OrderService.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/OrderService");
        return rmiProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanRestaurant() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(ro.ubb.socket.common.service.RestaurantService.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/RestaurantService");
        return rmiProxyFactoryBean;
    }
}
