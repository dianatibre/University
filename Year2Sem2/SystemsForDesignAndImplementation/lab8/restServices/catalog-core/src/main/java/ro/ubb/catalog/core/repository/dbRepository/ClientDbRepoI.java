package ro.ubb.catalog.core.repository.dbRepository;

import org.springframework.data.jpa.repository.Query;
import ro.ubb.catalog.core.model.Address;
import ro.ubb.catalog.core.model.Client;
import ro.ubb.catalog.core.repository.RepoI;

import java.util.List;

public interface ClientDbRepoI extends RepoI<Client, Integer> {
    List<Client> findByName(String name);
    List<Client> findByOrderByName();

    List<Client> findByOrderByNameAscEmailAsc();
    List<Client> findByOrderByNameDescEmailDesc();
}
