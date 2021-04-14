package lab7.service;

import lab7.domain.Address;
import lab7.domain.Restaurant;
import lab7.domain.validators.RestaurantValidator;
import lab7.domain.validators.ValidatorException;
import lab7.repository.dbRepository.RestaurantDbRepoI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RestaurantService implements ServiceI<Integer, Restaurant> {

    private static final Logger LOG = LoggerFactory.getLogger(RestaurantService.class);

    @Autowired
    private RestaurantDbRepoI restaurantDbRepoI;

    @Autowired
    private RestaurantValidator restaurantValidator;

    @Override
    public Optional<Restaurant> add(Restaurant restaurant) throws ValidatorException {
        restaurantValidator.validate(restaurant);
        LOG.trace("add: restaurant={}", restaurant);
        Optional<Restaurant> a = restaurantDbRepoI.findById(restaurant.getId());

        if (a.isEmpty()) {
            restaurantDbRepoI.save(restaurant);
        }
        LOG.trace("add --- method finished");
        return a;
    }

    @Override
    public List<Restaurant> get() {
        LOG.trace("get --- method entered");
        List<Restaurant> result = StreamSupport.stream(restaurantDbRepoI.findAll().spliterator(), false).collect(Collectors.toList());
        LOG.trace("getAllRestaurants: result={}", result);
        return result;
    }

    @Override
    public Optional<Restaurant> delete(Integer integer) {
        LOG.trace("delete: id={}", integer);
        Optional<Restaurant> a = restaurantDbRepoI.findById(integer);
        restaurantDbRepoI.deleteById(integer);
        LOG.trace("delete --- method finished");
        return a;
    }

    @Override
    @Transactional
    public Optional<Restaurant> update(Restaurant restaurant) throws ValidatorException {
        restaurantValidator.validate(restaurant);
        LOG.trace("update: restaurant={}", restaurant);
        Optional<Restaurant> restaurant1 = restaurantDbRepoI.findById(restaurant.getId());

        restaurantDbRepoI.findById(restaurant.getId())
                .ifPresent(a -> {
                    a.setName(restaurant.getName());
                    a.setCapacity(restaurant.getCapacity());
                    a.setDelivery(restaurant.getDelivery());
                    a.setRating(restaurant.getRating());
                    LOG.debug("updatedRestaurant: restaurant={}", a);
                });

        LOG.trace("upd --- method finished");
        return restaurant1;
    }

    @Override
    public List<Restaurant> filterFunction(Predicate<Restaurant> pred) {
        return this.restaurantDbRepoI.findAll().stream().filter(pred).collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> sortFunction(Comparator<Restaurant> pred) {
        return this.restaurantDbRepoI.findAll().stream().sorted(pred).collect(Collectors.toList());
    }

    @Override
    public void deleteAllById(Integer id) {

    }

    @Override
    public void deleteOrderWhenDeletingRestaurant(Integer id) {

    }

    @Override
    public void deleteOrderWhenDeletingDish(Integer id) {

    }

    @Override
    public void deleteOrderWhenDeletingClient(Integer id) {
    }

    public static Logger getLOG() {
        return LOG;
    }
}
