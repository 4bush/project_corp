package edu.css.purchases.config;

import edu.css.purchases.domain.RequestStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "edu.css.purchases")
@EntityScan("edu.css.purchases.persistence")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  CommandLineRunner demo(RequestService service) {
    return args -> System.out.println(
        "DRAFT -> " + service.move(RequestStatus.DRAFT, RequestStatus.APPROVED));
  }
}
