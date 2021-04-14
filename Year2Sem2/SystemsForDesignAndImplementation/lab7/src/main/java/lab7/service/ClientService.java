package lab7.service;

import lab7.domain.Address;
import lab7.domain.Client;
import lab7.domain.validators.ClientValidator;
import lab7.domain.validators.ValidatorException;
import lab7.repository.dbRepository.AddressDbRepoI;
import lab7.repository.dbRepository.ClientDbRepoI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ClientService implements ServiceI<Integer, Client> {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientDbRepoI clientDbRepoI;

    @Autowired
    private ClientValidator clientValidator;

    @Autowired
    private AddressDbRepoI addressDbRepoI;

    @Override
    public Optional<Client> add(Client client) throws ValidatorException {
        clientValidator.validate(client);
        logger.trace("add: client{}", client);
        Optional<Client> client1 = clientDbRepoI.findById(client.getId());
        Optional<Address> address=addressDbRepoI.findById(client.getAddress());

        if (client1.isEmpty() && address.isPresent()) {
            clientDbRepoI.save(client);
        }
        if(address.isEmpty())
            return Optional.of(client);
        logger.trace("add --- method finished");
        return client1;
    }

    @Override
    public List<Client> get() {
        logger.trace("get --- method entered");
        List<Client> result = new ArrayList<>(clientDbRepoI.findAll());
        logger.trace("getAllClients: result={}", result);
        return result;
    }

    @Override
    public Optional<Client> delete(Integer integer) {
        logger.trace("delete: id={}", integer);
        Optional<Client> client = clientDbRepoI.findById(integer);
        clientDbRepoI.deleteById(integer);
        logger.trace("delete --- method finished");
        return client;
    }

    @Override
    @Transactional
    public Optional<Client> update(Client client) throws ValidatorException {
        clientValidator.validate(client);
        logger.trace("update: client={}", client);
        Optional<Client> client1 = clientDbRepoI.findById(client.getId());
        Optional<Address> address=addressDbRepoI.findById(client.getAddress());
        if(address.isEmpty()){
            return Optional.empty();
        }

        clientDbRepoI.findById(client.getId())
                .ifPresent(c -> {
                    c.setName(client.getName());
                    c.setAddress(client.getAddress());
                    c.setAge(client.getAge());
                    c.setEmail(client.getEmail());
                    logger.debug("updateClient: client={}", c);
                });

        logger.trace("upd --- method finished");
        return client1;
    }

    @Override
    public List<Client> filterFunction(Predicate<Client> pred) {
        return this.clientDbRepoI.findAll().stream().filter(pred).collect(Collectors.toList());
    }

    @Override
    public List<Client> sortFunction(Comparator<Client> pred) {
        return this.clientDbRepoI.findAll().stream().sorted(pred).collect(Collectors.toList());
    }

    public static Logger getLOG() {
        return logger;
    }

    @Override
    public void deleteAllById(Integer id){
        Set<Client> allClients = this.clientDbRepoI.findAll().stream().filter(x->x.getAddress().equals(id)).collect(Collectors.toSet());
        allClients.forEach(x->this.clientDbRepoI.delete(x));
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
}
