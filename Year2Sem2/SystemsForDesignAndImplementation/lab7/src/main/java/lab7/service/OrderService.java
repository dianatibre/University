package lab7.service;

import lab7.domain.Client;
import lab7.domain.Dish;
import lab7.domain.Orders;
import lab7.domain.Restaurant;
import lab7.domain.validators.OrderValidator;
import lab7.domain.validators.ValidatorException;
import lab7.repository.dbRepository.ClientDbRepoI;
import lab7.repository.dbRepository.DishDbRepoI;
import lab7.repository.dbRepository.OrderDbRepoI;
import lab7.repository.dbRepository.RestaurantDbRepoI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrderService implements ServiceI<Integer, Orders> {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderDbRepoI orderDbRepoI;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private RestaurantDbRepoI restaurantDbRepoI;

    @Autowired
     private DishDbRepoI dishDbRepoI;

    @Autowired
    private ClientDbRepoI clientDbRepoI;

    @Override
    public Optional<Orders> add(Orders order) throws ValidatorException {
        orderValidator.validate(order);
        logger.trace("add: order{}", order);
        Optional<Orders> order1 = orderDbRepoI.findById(order.getId());
        Optional<Restaurant> restaurant=restaurantDbRepoI.findById(order.getRestaurantID());
        Optional<Dish> dish=dishDbRepoI.findById(order.getDishID());
        Optional<Client> client=clientDbRepoI.findById(order.getClientID());


        // you should add  ( ... && restaurant.isPresent() && dish.isPresent())
        if (order1.isEmpty() && client.isPresent() && restaurant.isPresent() && dish.isPresent()) {
            orderDbRepoI.save(order);
        }

        // you should add  ( ... && restaurant.isEmpty() || dish.isEmpty())
        if(client.isEmpty() || restaurant.isEmpty() || dish.isEmpty())
           return Optional.of(order);
        logger.trace("add --- method finished");
        return order1;

    }

    @Override
    public List<Orders> get() {
        logger.trace("get --- method entered");
        List<Orders> result = new ArrayList<>(orderDbRepoI.findAll());
        logger.trace("getAllOrders: result={}", result);
        return result;
    }


    @Override
    public Optional<Orders> delete(Integer integer) {
        logger.trace("delete: id={}", integer);
        Optional<Orders> order = orderDbRepoI.findById(integer);
        orderDbRepoI.deleteById(integer);
        logger.trace("delete --- method finished");
        return order;
    }

    @Override
    @Transactional
    public Optional<Orders> update(Orders order) throws ValidatorException {
        orderValidator.validate(order);
        logger.trace("update: order={}", order);
        Optional<Orders> order1 = orderDbRepoI.findById(order.getId());
        Optional<Client> client=clientDbRepoI.findById(order.getClientID());
        Optional<Restaurant> restaurant=restaurantDbRepoI.findById(order.getRestaurantID());
        Optional<Dish> dish= dishDbRepoI.findById(order.getDishID());

        // you should add ( .. || restaurant.isEmpty() || dish.isEmpty() )
        if(client.isEmpty() || restaurant.isEmpty() || dish.isEmpty()){
            return Optional.empty();
        }

        orderDbRepoI.findById(order.getId())
                .ifPresent(o -> {
                    o.setClientID(order.getClientID());
                    o.setRestaurantID(order.getRestaurantID());
                    o.setDishID(order.getDishID());
                    o.setDate(order.getDate());
                    o.setPaymentType(order.getPaymentType());
                    logger.debug("updateOrder: order={}", o);
                });

        logger.trace("upd --- method finished");
        return order1;
    }

    @Override
    public List<Orders> filterFunction(Predicate<Orders> pred) {
        return this.orderDbRepoI.findAll().stream().filter(pred).collect(Collectors.toList());
    }

    @Override
    public List<Orders> sortFunction(Comparator<Orders> pred) {
        return this.orderDbRepoI.findAll().stream().sorted(pred).collect(Collectors.toList());
    }

    public static Logger getLOG() {
        return logger;
    }

    @Override
    public void deleteAllById(Integer id){
        Set<Client> allClients = this.clientDbRepoI.findAll().stream().filter(x->x.getAddress().equals(id)).collect(Collectors.toSet());
        allClients.forEach(x->this.deleteOrderWhenDeletingClient(x.getId()));
    }

    @Override
    public void deleteOrderWhenDeletingRestaurant(Integer id) {
        Set<Orders> allOrders= this.orderDbRepoI.findAll().stream().filter(x->x.getRestaurantID().equals(id)).collect(Collectors.toSet());
        allOrders.forEach(x->this.orderDbRepoI.delete(x));
    }

    @Override
    public void deleteOrderWhenDeletingDish(Integer id) {
        Set<Orders> allOrders= this.orderDbRepoI.findAll().stream().filter(x->x.getDishID().equals(id)).collect(Collectors.toSet());
        allOrders.forEach(x->this.orderDbRepoI.delete(x));
    }

    @Override
    public void deleteOrderWhenDeletingClient(Integer id) {
        Set<Orders> allOrders = this.orderDbRepoI.findAll().stream().filter(x->x.getClientID().equals(id)).collect(Collectors.toSet());
        allOrders.forEach(x->this.orderDbRepoI.delete(x));
    }
}
