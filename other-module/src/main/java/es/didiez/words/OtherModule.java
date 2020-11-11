package es.didiez.words;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@Join(path = "/", to = "/index.xhtml")
public class OtherModule {
}
