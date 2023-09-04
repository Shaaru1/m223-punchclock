package ch.zli.m223.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.User;

@ApplicationScoped
public class UserService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public User createUser(User user) {
        // Validierung und Geschäftslogik, bevor der Benutzer in die Datenbank gespeichert wird
        entityManager.persist(user);
        return user;
    }

    public User getUser(Long userId) {
        return entityManager.find(User.class, userId);
    }

    @Transactional
    public User updateUser(User user) {
        // Validierung und Geschäftslogik, bevor der Benutzer in die Datenbank aktualisiert wird
        return entityManager.merge(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = getUser(userId);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    public User getUserByEmail(String email) {
        // Annahme: Die E-Mail-Adresse ist in der Datenbank eindeutig
        return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
}