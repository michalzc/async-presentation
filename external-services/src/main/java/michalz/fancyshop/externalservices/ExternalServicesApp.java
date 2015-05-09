package michalz.fancyshop.externalservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by michal on 09.05.15.
 */
@EnableAutoConfiguration
@ComponentScan
public class ExternalServicesApp {
    public static void main(String[] args) {
        SpringApplication.run(ExternalServicesApp.class, args);
    }
}
