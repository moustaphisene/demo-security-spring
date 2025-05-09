package sen.bank.demospring6security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
//@EntityScan("sen.bank.demospring6security.entity")
//@EnableJpaRepositories("sen.bank.demospring6security.repository")
//@EnableWebSecurity
public class DemoSpring6SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpring6SecurityApplication.class, args);
    }

}
