package ch.zli.m223.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.Booking;

@ApplicationScoped
public class BookingService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public Booking createBooking(Booking booking) {
        // Validierung und Geschäftslogik, bevor die Buchung in die Datenbank gespeichert wird
        entityManager.persist(booking);
        return booking;
    }

    public Booking getBooking(Long bookingId) {
        return entityManager.find(Booking.class, bookingId);
    }

    @Transactional
    public Booking updateBooking(Booking booking) {
        // Validierung und Geschäftslogik, bevor die Buchung in die Datenbank aktualisiert wird
        return entityManager.merge(booking);
    }

    @Transactional
    public void deleteBooking(Long bookingId) {
        Booking booking = getBooking(bookingId);
        if (booking != null) {
            entityManager.remove(booking);
        }
    }
}
