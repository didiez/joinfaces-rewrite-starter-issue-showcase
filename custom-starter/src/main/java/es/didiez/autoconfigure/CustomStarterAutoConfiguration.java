package es.didiez.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class CustomStarterAutoConfiguration {
    
    @Bean
    String springApplicationName(@Value("${spring.application.name}") String springApplicationName) {
        return springApplicationName;
    }

}
