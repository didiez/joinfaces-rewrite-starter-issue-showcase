package es.didiez.words;

import javax.enterprise.context.RequestScoped;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.stereotype.Component;

/**
 *
 * @author didiez
 */
@Data
@Component
@RequestScoped
@Join(path = "/words", to = "/words.xhtml")
public class WordsBean {
 
    String word = RandomStringUtils.randomPrint(5);
    
    public void requestAnotherWord(){
        word = RandomStringUtils.randomPrint(5);
    }
}
