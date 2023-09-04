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

import ch.zli.m223.model.Booking;
import ch.zli.m223.service.BookingService;

@Path("/bookings")
public class BookingController {

    @Inject
    BookingService bookingService;

    // Endpunkt zum Erstellen einer neuen Buchung
    @POST
    @RolesAllowed({"admin", "member"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createBooking(@Valid Booking booking) {
        Booking newBooking = bookingService.createBooking(booking);
        return Response.status(Response.Status.CREATED).entity(newBooking).build();
    }

    // Endpunkt zum Abrufen von Informationen zu einer bestimmten Buchung anhand ihrer ID
    @GET
    @Path("/{bookingId}")
    @RolesAllowed({"admin", "member"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooking(@PathParam("bookingId") Long bookingId) {
        Booking booking = bookingService.getBooking(bookingId);
        if (booking != null) {
            return Response.ok(booking).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Endpunkt zum Aktualisieren einer Buchung
    @PUT
    @Path("/{bookingId}")
    @RolesAllowed({"admin", "member"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateBooking(@PathParam("bookingId") Long bookingId, @Valid Booking updatedBooking) {
        Booking existingBooking = bookingService.getBooking(bookingId);
        if (existingBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        // Validierung und Aktualisierung der Buchungsdetails
        existingBooking.setDate(updatedBooking.getDate());
        existingBooking.setHalfDay(updatedBooking.isHalfDay());
        existingBooking.setReason(updatedBooking.getReason());
        existingBooking.setStatus(updatedBooking.getStatus());
        
        Booking updated = bookingService.updateBooking(existingBooking);
        return Response.ok(updated).build();
    }
    
    // Weitere Endpunkte für die Löschung von Buchungen und andere Buchungsaktionen können hinzugefügt werden
    // Endpunkt zum Löschen einer Buchung anhand ihrer ID
    @DELETE
    @Path("/{bookingId}")
    @RolesAllowed({"admin", "member"})
    @Transactional
    public Response deleteBooking(@PathParam("bookingId") Long bookingId) {
        Booking booking = bookingService.getBooking(bookingId);
        if (booking != null) {
            bookingService.deleteBooking(bookingId);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
