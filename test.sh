# clean and build
for project in custom-starter other-module app ; do mvn clean install -f $project/pom.xml; done;

# enable/disable default annotation config provider
if [[ $1 == 'custom' ]]
then
    enabled=false  # No issues if enabled=false, using our custom rewrite annotation provider as fallback.
else
    enabled=true   # OOME if enabled=true and -Xmx150m.
fi

# run 
JAVA_OPTS="-Xmx150m" ./app/target/app-0.0.1-SNAPSHOT.jar --joinfaces.rewrite.annotation-config-provider.enabled=$enabled