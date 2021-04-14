package lab7.service;

import lab7.domain.Address;
import lab7.domain.Dish;
import lab7.domain.validators.AddressValidator;
import lab7.domain.validators.DishValidator;
import lab7.domain.validators.ValidatorException;
import lab7.repository.dbRepository.AddressDbRepoI;
import lab7.repository.dbRepository.DishDbRepoI;
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
public class DishService implements ServiceI<Integer, Dish> {

    private static final Logger LOG = LoggerFactory.getLogger(DishService.class);

    @Autowired
    private DishDbRepoI dishDbRepoI;

    @Autowired
    private DishValidator dishValidator;

    @Override
    public Optional<Dish> add(Dish dish) throws ValidatorException {
        dishValidator.validate(dish);
        LOG.trace("add: dish={}", dish);
        Optional<Dish> a = dishDbRepoI.findById(dish.getId());

        if (a.isEmpty()) {
            dishDbRepoI.save(dish);
        }
        LOG.trace("add --- method finished");
        return a;
    }

    @Override
    public List<Dish> get() {
        LOG.trace("get --- method entered");
        List<Dish> result = StreamSupport.stream(dishDbRepoI.findAll().spliterator(), false).collect(Collectors.toList());
        LOG.trace("getAllDishes: result={}", result);
        return result;
    }

    @Override
    public Optional<Dish> delete(Integer integer) {
        LOG.trace("delete: id={}", integer);
        Optional<Dish> a = dishDbRepoI.findById(integer);
        dishDbRepoI.deleteById(integer);
        LOG.trace("delete --- method finished");
        return a;
    }

    @Override
    @Transactional
    public Optional<Dish> update(Dish dish) throws ValidatorException {
        dishValidator.validate(dish);
        LOG.trace("update: dish={}", dish);
        Optional<Dish> dish1 = dishDbRepoI.findById(dish.getId());

        dishDbRepoI.findById(dish.getId())
                .ifPresent(a -> {
                    a.setCategory(dish.getCategory());
                    a.setName(dish.getName());
                    a.setPrice(dish.getPrice());
                    a.setQuantity(dish.getQuantity());
                    LOG.debug("updatedDish: dish={}", a);
                });

        LOG.trace("upd --- method finished");
        return dish1;
    }

    @Override
    public List<Dish> filterFunction(Predicate<Dish> pred) {
        return this.dishDbRepoI.findAll().stream().filter(pred).collect(Collectors.toList());
    }

    @Override
    public List<Dish> sortFunction(Comparator<Dish> pred) {
        return this.dishDbRepoI.findAll().stream().sorted(pred).collect(Collectors.toList());
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
