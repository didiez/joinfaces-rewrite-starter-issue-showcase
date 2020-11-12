package es.didiez.autoconfigure;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import org.ocpsoft.common.services.ServiceLoader;
import org.ocpsoft.rewrite.annotation.ClassVisitorImpl;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.api.ClassVisitor;
import org.ocpsoft.rewrite.annotation.scan.AbstractClassFinder;
import org.ocpsoft.rewrite.annotation.scan.ByteCodeFilter;
import org.ocpsoft.rewrite.annotation.scan.PackageFilter;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author didiez
 */
@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CustomRewriteAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "joinfaces.rewrite.annotation-config-provider.enabled", havingValue = "false")
    CustomSpringBootAnnotationConfigProvider customRewriteAnnotationConfigProvider() {
        return new CustomSpringBootAnnotationConfigProvider();
    }

    class CustomSpringBootAnnotationConfigProvider extends HttpConfigurationProvider {

        @Override
        public int priority() {
            return 100;
        }

        @Override
        public org.ocpsoft.rewrite.config.Configuration getConfiguration(ServletContext servletContext) {

            // the byte code filter needs to know the annotations to look for
            Set<Class<? extends Annotation>> annotationType = new LinkedHashSet<>();

            // list of annotation handlers for the ClassVisitor
            List<AnnotationHandler<Annotation>> annotationHandlers = new ArrayList<>();

            // load the implementations of the AnnotationHandler SPI
            Iterator<AnnotationHandler> handlerIterator = ServiceLoader.load(AnnotationHandler.class).iterator();
            while (handlerIterator.hasNext()) {
                AnnotationHandler<Annotation> handler = handlerIterator.next();
                annotationHandlers.add(handler);
                annotationType.add(handler.handles());
            }

            // this class will identify the classes that should be scanned without loading them
            ByteCodeFilter byteCodeFilter = new ByteCodeFilter(annotationType);
            PackageFilter packageFilter = new PackageFilter(null);

            // ClassVisitor will process all classes that ByteCodeFilter considers as worth scanning them
            ClassVisitorImpl classVisitor = new ClassVisitorImpl(annotationHandlers, servletContext);

            // fallback to some other classloder if there is no context class loader
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            if (classloader == null) {
                classloader = this.getClass().getClassLoader();
            }

            new SpringBootWebClassesFinder(servletContext, classloader, packageFilter, byteCodeFilter)
                    .findClasses(classVisitor);

            // return the rules collected by the class visitor
            return classVisitor;
        }

        class SpringBootWebClassesFinder extends AbstractClassFinder {

            public SpringBootWebClassesFinder(ServletContext servletContext, ClassLoader classLoader, PackageFilter packageFilter,
                    ByteCodeFilter byteCodeFilter) {
                super(servletContext, classLoader, packageFilter, byteCodeFilter);
            }

            @Override
            public void findClasses(ClassVisitor visitor) {
                new Reflections(buildConfigurationBuilder())
                        .getTypesAnnotatedWith(Join.class, true)
                        .stream()
                        .forEach(visitor::visit);
            }

            private ConfigurationBuilder buildConfigurationBuilder() {
                ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().setExpandSuperTypes(false);

                // stores collections of urls to be scanned
                Collection<URL> result = new ArrayList<>();
                Collection<String> strings = new HashSet<>();

                // get only urls of libraries that contains jsf types
                add(result, strings, ClasspathHelper.forResource("META-INF/faces-config.xml", this.getClass().getClassLoader()));

                // add project classes and resources folder
                for (URL url : ClasspathHelper.forManifest()) {
                    String file = url.getFile();
                    // check if running debug/test or uber jar
                    if (!(file.endsWith(".jar") || file.endsWith(".jar!/") || file.endsWith(".war"))) {
                        add(result, strings, url);
                    }
                }

                configurationBuilder.setUrls(result);

                return configurationBuilder;
            }

            private void add(Collection<URL> urls, Collection<String> strings, Collection<URL> newURLs) {
                for (URL url : newURLs) {
                    add(urls, strings, url);
                }
            }

            private void add(Collection<URL> urls, Collection<String> strings, URL url) {
                String string = url.toString();
                if (!strings.contains(string)) {
                    urls.add(url);
                    strings.add(string);
                }
            }

            @Override
            public int priority() {
                return 0;
            }

        }

    }
}
