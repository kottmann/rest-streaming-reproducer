package response.in;

import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/hello")
public class GreetingResource {

  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response hello(@RestQuery String test, @Context HttpServerRequest request) {
    return Response.ok(new GreetingInputStream(request, 50_000_000, test)).build();
  }
}
