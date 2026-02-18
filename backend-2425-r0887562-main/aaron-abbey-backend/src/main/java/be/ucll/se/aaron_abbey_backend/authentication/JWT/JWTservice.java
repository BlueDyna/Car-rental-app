package be.ucll.se.aaron_abbey_backend.authentication.JWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.User;


import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

@Service
public class JWTservice {


    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JWTservice() {
        String secretKey = "Q3O7EwGZ9ZigSKKrF+2wvMjX1Qs7vZa3B+L7g6Z6oRJxMjzKkZ6+Q3O76wGZ9Zig";
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.verifier = JWT.require(this.algorithm).build();
    }

    public String createToken(String subject, String role, Long id) {
        int timeToExpire = 1000 * 60 * 60 * 24; // 24 hours in milliseconds
        // int timeToExpire = 1000 * 60; // 1 minute for testing
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + timeToExpire);
        return JWT.create()
                .withSubject(subject)
                .withClaim("role", role)
                .withClaim("id", id)
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .sign(this.algorithm);
    }

    public void verifyToken(String token) throws ServiceException {
        this.verifier.verify(token);

        try {
            JWT.require(this.algorithm)
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException("token", "token.invalid");
        }
    }

    public String getSubjectFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    public String getRoleFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("role").asString();
    }

    public UserDetails loadUserBySubjectAndRole(String subject, String role) {
        // This is a very basic example. In a real application, you would probably
        // want to load the user details from your database based on the subject and
        // role.
        return User.withUsername(subject)
                .password("") // You can leave the password empty since you're using JWT for authentication
                .roles(role)
                .build();
    }

    public Boolean verifyRole(String token, String expectedRole) {
        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        Map<String, Claim> claims = verifier.verify(token).getClaims();
        String actualRole = claims.get("role").toString().replace("\"", "");
        return actualRole.equals(expectedRole);

    }

    public Long getIdFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("id").asLong();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
