package es.didiez.app;

import es.didiez.words.OtherModule;
import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Join(path = "/", to = "/index.xhtml")
@Import(OtherModule.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
