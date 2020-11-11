package es.didiez.app;

import java.util.Random;
import javax.enterprise.context.RequestScoped;
import lombok.Data;
import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.stereotype.Component;

/**
 *
 * @author didiez
 */
@Data
@Component
@RequestScoped
@Join(path = "/magic", to = "/index.xhtml")
public class IndexBean {
 
    private static final Random random = new Random();
    
    Integer magicNumber = random.nextInt(100);
    
    public void requestAnotherMagicNumber(){
        magicNumber = random.nextInt(100);
    }
}
