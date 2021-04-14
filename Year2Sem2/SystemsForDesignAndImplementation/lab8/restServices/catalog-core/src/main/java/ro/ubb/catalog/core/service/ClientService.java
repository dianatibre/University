package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Address;
import ro.ubb.catalog.core.model.Client;
import ro.ubb.catalog.core.model.Orders;
import ro.ubb.catalog.core.model.validators.ClientValidator;
import ro.ubb.catalog.core.model.validators.ValidatorException;
import ro.ubb.catalog.core.repository.dbRepository.AddressDbRepoI;
import ro.ubb.catalog.core.repository.dbRepository.ClientDbRepoI;
import ro.ubb.catalog.core.repository.dbRepository.OrderDbRepoI;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClientService implements ServiceI<Integer, Client> {

    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientDbRepoI clientDbRepoI;

    @Autowired
    private ClientValidator clientValidator;

    @Autowired
    private OrderDbRepoI orderDbRepoI;

    @Autowired
    @Qualifier("addressDbRepoI")
    private AddressDbRepoI addressDbRepoI;

    @Override
    public Optional<Client> add(Client client) throws ValidatorException {
        clientValidator.validate(client);
        LOG.trace("add: client={}", client);
        Optional<Client> a = clientDbRepoI.findById(client.getId());
        Optional<Address> address = addressDbRepoI.findById(client.getAddress());

        try {
            a.orElseThrow(Exception::new);
            address.orElseThrow(Exception::new);
        } catch (Exception e) {
            clientDbRepoI.save(client);
            return a;
        }
        LOG.trace("add --- method finished");
        return a;
    }

    @Override
    public List<Client> get() {
        LOG.trace("get --- method entered");
        List<Client> result = StreamSupport.stream(clientDbRepoI.findAll().spliterator(), false).collect(Collectors.toList());
        LOG.trace("getAllClients: result={}", result);
        return result;
    }

    @Override
    public Optional<Client> delete(Integer integer) {
        LOG.trace("delete: id={}", integer);
        Optional<Client> a = clientDbRepoI.findById(integer);
        Set<Orders> allOrders = this.orderDbRepoI.findAll().stream().filter(x -> x.getClientID().equals(integer)).collect(Collectors.toSet());
        allOrders.forEach(x -> this.orderDbRepoI.delete(x));
        clientDbRepoI.deleteById(integer);
        LOG.trace("delete --- method finished");
        return a;
    }

    @Override
    @Transactional
    public Optional<Client> update(Client client) throws ValidatorException {
        clientValidator.validate(client);
        LOG.trace("update: client={}", client);
        Optional<Client> client1 = clientDbRepoI.findById(client.getId());
        Optional<Address> address = addressDbRepoI.findById(client.getAddress());
        try {
            client1.orElseThrow(Exception::new);
            address.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can not update this client! Nonexistent id!");
            return client1;
        }
        clientDbRepoI.findById(client.getId())
                .ifPresent(a -> {
                    a.setName(client.getName());
                    a.setAge(client.getAge());
                    a.setAddress(client.getAddress());
                    a.setEmail(client.getEmail());
                    LOG.debug("updatedClient: client={}", a);
                });

        LOG.trace("upd --- method finished");
        return client1;
    }

    @Override
    public List<Client> filterFunction(String name) {
        return this.clientDbRepoI.findByName(name);
    }

    @Override
    public List<Client> sortFunction() {
        return this.clientDbRepoI.findByOrderByName();
    }

    @Override
    public List<Client> sortMultipleFunction() {
        return this.clientDbRepoI.findByOrderByNameAscEmailAsc();
    }

    @Override
    public List<Client> sortMultipleFunctionDesc() {
        return this.clientDbRepoI.findByOrderByNameDescEmailDesc();
    }

    public static Logger getLOG() {
        return LOG;
    }
}
