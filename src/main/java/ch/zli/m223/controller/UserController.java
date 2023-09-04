package ch.zli.m223.controller;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.zli.m223.model.User;
import ch.zli.m223.service.UserService;

@Path("/users")
public class UserController {
    @Inject
    UserService userService;

    // Endpunkt zum Erstellen eines neuen Benutzers
    @POST
    @RolesAllowed("admin") // Nur Administratoren haben Zugriff
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createUser(@Valid User user) {
        User newUser = userService.createUser(user);
        return Response.status(Response.Status.CREATED).entity(newUser).build();
    }

    // Endpunkt zum Abrufen von Informationen zu einem bestimmten Benutzer anhand seiner ID
    @GET
    @Path("/{userId}")
    @RolesAllowed({"admin", "member"}) // Administratoren und Mitglieder haben Zugriff
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") Long userId) {
        User user = userService.getUser(userId);
        if (user != null) {
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Endpunkt zum Aktualisieren eines bestimmten Benutzers
    @PUT
    @Path("/{userId}")
    @RolesAllowed("admin") // Nur Administratoren können Benutzer aktualisieren
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateUser(@PathParam("userId") Long userId, @Valid User updatedUser) {
        User user = userService.getUser(userId);
        if (user != null) {
            // Aktualisieren Sie die Benutzerdaten basierend auf den Werten in updatedUser
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());

            // Speichern Sie die aktualisierten Benutzerdaten in der Datenbank
            userService.updateUser(user);

            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Endpunkt zum Löschen eines bestimmten Benutzers
    @DELETE
    @Path("/{userId}")
    @RolesAllowed("admin") // Nur Administratoren können Benutzer löschen
    @Transactional
    public Response deleteUser(@PathParam("userId") Long userId) {
        User user = userService.getUser(userId);
        if (user != null) {
            // Löschen Sie den Benutzer aus der Datenbank
            userService.deleteUser(userId);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
