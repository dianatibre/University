package lab7.service;

import lab7.domain.Address;
import lab7.domain.Client;
import lab7.domain.validators.AddressValidator;
import lab7.domain.validators.ValidatorException;
import lab7.repository.dbRepository.AddressDbRepoI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AddressService implements ServiceI<Integer, Address> {

    private static final Logger LOG = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    private AddressDbRepoI addressDbRepoI;

    @Autowired
    private AddressValidator addressValidator;

    @Override
    public Optional<Address> add(Address address) throws ValidatorException {
        addressValidator.validate(address);
        LOG.trace("add: address={}", address);
        Optional<Address> a = addressDbRepoI.findById(address.getId());

        if (a.isEmpty()) {
            addressDbRepoI.save(address);
        }
        LOG.trace("add --- method finished");
        return a;
    }

    @Override
    public List<Address> get() {
        LOG.trace("get --- method entered");
        List<Address> result = StreamSupport.stream(addressDbRepoI.findAll().spliterator(), false).collect(Collectors.toList());
        LOG.trace("getAllAddresses: result={}", result);
        return result;
    }

    @Override
    public Optional<Address> delete(Integer integer) {
        LOG.trace("delete: id={}", integer);
        Optional<Address> a = addressDbRepoI.findById(integer);
        addressDbRepoI.deleteById(integer);
        LOG.trace("delete --- method finished");
        return a;
    }

    @Override
    @Transactional
    public Optional<Address> update(Address address) throws ValidatorException {
        addressValidator.validate(address);
        LOG.trace("update: address={}", address);
        Optional<Address> address1 = addressDbRepoI.findById(address.getId());

        addressDbRepoI.findById(address.getId())
                .ifPresent(a -> {
                    a.setCity(address.getCity());
                    a.setStreet(address.getStreet());
                    a.setNumber(address.getNumber());
                    a.setZipCode(address.getZipCode());
                    LOG.debug("updatedAddress: address={}", a);
                });

        LOG.trace("upd --- method finished");
        return address1;
    }

    @Override
    public List<Address> filterFunction(Predicate<Address> pred) {
        return this.addressDbRepoI.findAll().stream().filter(pred).collect(Collectors.toList());
    }

    @Override
    public List<Address> sortFunction(Comparator<Address> pred) {
        return this.addressDbRepoI.findAll().stream().sorted(pred).collect(Collectors.toList());
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
