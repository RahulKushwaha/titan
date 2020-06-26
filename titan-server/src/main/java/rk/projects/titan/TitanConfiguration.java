package rk.projects.titan;

import static com.google.common.base.Preconditions.checkNotNull;

import rk.projects.titan.backend.DatabaseBackend;
import rk.projects.titan.backend.DatabaseConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import org.immutables.value.Value;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.jdbi.v3.core.Jdbi;

/**
 * Server configuration for the titan  API server.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableTitanConfiguration.class)
public abstract class TitanConfiguration extends Configuration {
    
    @JsonProperty("database")
    abstract DatabaseConfiguration getDatabaseConfiguration();

    public Jdbi createDBI(Environment environment, String name) {
        final JdbiFactory factory = new JdbiFactory();
        return factory.build(environment, getDatabaseConfiguration().getDataSourceFactory(), name);
    }

    public DatabaseBackend getDatabaseBackend(Environment environment) {
        return createDBI(environment, "jdbi-backend").onDemand(DatabaseBackend.class);
    }
}
