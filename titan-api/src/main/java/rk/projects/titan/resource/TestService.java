package rk.projects.titan.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("test-service")
public interface TestService {

  @GET
  @Path("/ping")
  @Consumes("application/json")
  @Produces("application/json")
  String ping(@QueryParam("pong") String pong);

}
