package es.didiez.autoconfigure;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import lombok.Data;
import org.ocpsoft.rewrite.annotation.Join;
import org.primefaces.PrimeFaces;
import org.springframework.stereotype.Component;

/**
 *
 * @author didiez
 */
@Data
@Component
@RequestScoped
@Join(path = "/debug", to = "/debug.xhtml")
public class DebugBean {

    private String name;
    
    public void sayHi(){
        PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Hi " + name + "!"));
    }
    
}
