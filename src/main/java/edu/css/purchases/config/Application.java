package edu.css.purchases.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "edu.css.purchases")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
