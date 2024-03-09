package academy.devdojo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.outside.Connection;

@Configuration
public class BeanConfig {

    @Bean
    public Connection connectionMySql (){
        return new Connection("localhost", "user", "xxxx");
    }


    @Bean(name="mongoDb")
    public Connection connectionMongoDb (){
        return new Connection("localhost", "mongoDb", "xxxx");
    }
}

