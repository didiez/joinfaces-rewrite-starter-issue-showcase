package es.didiez.autoconfigure;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.handler.HandlerWeights;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.param.RegexConstraint;

/**
 * 
 * @author didiez
 */
public class JoinMatchesHandler implements AnnotationHandler<JoinMatches> {

    private final Logger log = Logger.getLogger(JoinMatchesHandler.class);
    
    @Override
    public Class<JoinMatches> handles() {
        return JoinMatches.class;
    }

    @Override
    public int priority() {
        return HandlerWeights.WEIGHT_TYPE_ENRICHING;
    }

    @Override
    public void process(ClassContext context, JoinMatches annotation, HandlerChain chain) {
        // add a corresponding RegexConstraint
        String expr = annotation.matches();
        context.getRuleBuilder().where(annotation.param()).constrainedBy(new RegexConstraint(expr));
        
        if (log.isTraceEnabled()) {
           log.trace("Parameter [{}] has been constrained by [{}]", annotation.param(), expr);
        }
        
        chain.proceed();
    }

}
