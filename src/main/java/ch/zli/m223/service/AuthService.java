package ch.zli.m223.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.jwt.JsonWebToken;

import ch.zli.m223.model.User;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;

@ApplicationScoped
public class AuthService {

    @Inject
    UserService userService; // Annahme, dass es einen UserService gibt

    @Inject
    JsonWebToken jwt;

    @Inject
    SecurityIdentity securityIdentity;

    // Registrierung eines Benutzers
    @Transactional
    public User register(User user) {
        // Überprüfen, ob die E-Mail-Adresse bereits existiert
        if (userService.getUserByEmail(user.getEmail()) != null) {
            return null; // Benutzer existiert bereits
        }

        // Annahme: Passwort-Hashing sollte hier erfolgen
        // Speichern Sie den Benutzer in der Datenbank
        return userService.createUser(user);
    }

    // Anmeldung eines Benutzers und Generierung eines JWT-Tokens
    public String login(String email, String password) {
        // Annahme: Implementieren Sie hier die Authentifizierung des Benutzers
        User user = userService.getUserByEmail(email);

        // Überprüfen Sie die Anmeldeinformationen (Passwort-Hashing sollte erfolgen)
        if (user != null && user.getPassword().equals(password)) {
            // Erzeugen Sie das JWT-Token
            return generateJWTToken(user);
        }
        return null; // Anmeldung fehlgeschlagen
    }

    // Generieren eines JWT-Tokens für einen Benutzer
    public String generateJWTToken(User user) {
        JwtClaimsBuilder claims = Jwt.claims();
        claims.subject(user.getId().toString());
        claims.groups(user.getRole().toString());

        return Jwt.preferredUserName(user.getEmail())
                  .issuer("your-issuer")
                  .upn(user.getEmail())
                  .groups(user.getRole().toString())
                  .expiresIn(1800) // Gültigkeit: 30 Minuten (passen Sie dies an)
                  .sign();
    }
}
