package guru.bug.courses.srs.control.token;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Singleton
@RegisterRestClient(configKey="token-api")
public interface TokenResource {

    @POST
    @Path("/tokens")
    Token getToken(Auth auth);
}
