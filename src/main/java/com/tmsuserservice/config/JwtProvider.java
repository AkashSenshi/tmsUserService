package com.tmsuserservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtProvider {
    public static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    public static String generateToken(Authentication auth){
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email",auth.getName())
                .claim("authorities",roles)
                .signWith(key)
                .compact();

        return jwt;
    }

    public static String getEmailFromJwtToken(String jwt) {
        jwt = jwt.substring(7);
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }
    public static String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> auths = new HashSet<>();

        for(GrantedAuthority authority : collection){
            auths.add(authority.getAuthority());
        }
        return String.join(",",auths);
    }
}

/*
* The `JwtProvider` class appears to handle the generation and extraction of JWT (JSON Web Token) tokens. Here's a breakdown of its functionality:

1. **Class Declaration**:
   - The class is declared as `public`, indicating that it can be accessed from outside its package.
   - Named `JwtProvider`, suggesting its purpose is to provide JWT-related functionalities.

2. **Fields**:
   - **key**:
     - A `public static` field representing the secret key used for signing and verifying JWT tokens.
     - Initialized with a value obtained from `Keys.hmacShaKeyFor()` method, using the secret key from `JwtConstant` class.
     - It's `public static`, meaning it's accessible without creating an instance of the class.

3. **Methods**:
   - **generateToken(Authentication auth)**:
     - A `public static` method responsible for generating a JWT token based on the provided `Authentication` object.
     - Extracts authorities from the `Authentication` object and converts them into a comma-separated string.
     - Constructs a JWT token using the `Jwts.builder()` method, setting issued and expiration dates, claims for email and authorities, and signing the token with the secret key.
     - Returns the generated JWT token.

   - **getEmailFromJwtToken(String jwt)**:
     - A `public static` method to extract the email claim from a JWT token.
     - Removes the "Bearer " prefix from the JWT token string.
     - Parses the JWT token using the `Jwts.parser()` method, setting the signing key, and retrieves the claims.
     - Returns the email claim extracted from the JWT token.

   - **populateAuthorities(Collection<? extends GrantedAuthority> collection)**:
     - A `public static` method to convert a collection of `GrantedAuthority` objects into a comma-separated string of roles.
     - Iterates through the collection of authorities, adding each authority's string representation to a set.
     - Joins the elements of the set into a single string separated by commas.
     - Returns the comma-separated string of roles.

4. **Purpose**:
   - `JwtProvider` encapsulates methods for generating and parsing JWT tokens, centralizing this functionality.
   - By providing static methods, it allows easy access to JWT-related functionalities without the need to instantiate the class.
   - This class facilitates token generation based on user authentication details and extraction of information from JWT tokens, such as the email and authorities.

Overall, `JwtProvider` is crucial for handling JWT-related operations within the application, promoting code reusability and maintainability.
* */