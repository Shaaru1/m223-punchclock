package ch.zli.m223.controller;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.zli.m223.model.User;
import ch.zli.m223.service.AuthService;
import ch.zli.m223.service.UserService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    EntityManager entityManager;

    @Inject
    AuthService authService;

    @Inject
    UserService userService;

    @POST
    @Path("/register")
    @PermitAll // Jeder kann sich registrieren
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        User registeredUser = authService.register(user);
        if (registeredUser != null) {
            // Erzeugen und zurückgeben Sie das JWT-Token
            String token = authService.generateJWTToken(registeredUser);
            return Response.status(Response.Status.CREATED).entity(token).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Die Registrierung ist fehlgeschlagen.").build();
    }

    // Endpunkt zur Anmeldung eines Benutzers
    @POST
    @Path("/login")
    public Response login(User loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        String token = authService.login(email, password);

        if (token != null) {
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Anmeldung fehlgeschlagen").build();
        }
    }
}
