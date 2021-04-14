package lab7.domain.validators;

import lab7.domain.Client;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClientValidator implements Validator<Client> {
    @Override
    public void validate(Client entity) throws ValidatorException {
        Optional<Integer> id = Optional.of(entity.getId());
        id.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Id must be a positive integer value!\n");
        });

        Optional<String> name = Optional.of(entity.getName());
        name.filter(x -> x.equals("")).ifPresent(s -> {
            throw new ValidatorException("Client name field cannot be empty!\n");
        });

        Optional<Integer> address = Optional.of(entity.getAddress());
        address.filter(x -> x <= 0).ifPresent(s -> {
            throw new ValidatorException("Client address must be a valid ID!\n");
        });

        Optional<Integer> age = Optional.of(entity.getAge());
        age.filter(x -> x <= 15).ifPresent(s -> {
            throw new ValidatorException("Age must be a greater or equal than 16!\n");
        });

        Optional<String> mail = Optional.of(entity.getEmail());
        mail.filter(x -> x.length() <= 5 || !x.contains("@")).ifPresent(s -> {
            throw new ValidatorException("Email address of the client must be a valid one, i.e. the length must be at least 5, and contain at least one '@'!\n");
        });
    }
}