package lab7.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"lab7.repository", "lab7.service", "lab7.ui", "lab7.domain.validators"})
public class RestaurantConfig {
}
