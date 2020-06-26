package rk.projects.titan.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.dropwizard.db.DataSourceFactory;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableDatabaseConfiguration.class)
public interface DatabaseConfiguration {

  @JsonProperty("connection")
  DataSourceFactory getDataSourceFactory();
}
