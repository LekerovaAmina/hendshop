package kz.handshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // @CreatedTimestamp @UpdateTimestamp
public class HandShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandShopApplication.class, args);
    }

}