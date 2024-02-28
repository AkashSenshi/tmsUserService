package com.tmsuserservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        if(jwt!=null){
//            Bearer jwt_string
            jwt = jwt.substring(7);
            System.out.println(jwt);
            try{
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(email,null,auths);


                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (Exception e){
                throw new BadCredentialsException("Invalid Token...");
            }
        }

        filterChain.doFilter(request,response);

    }
}

/*
* The `JwtTokenValidator` class extends `OncePerRequestFilter`, indicating that it's intended to be invoked once per request to validate JWT tokens. Let's analyze its functionality:

1. **Class Declaration**:
   - The class is named `JwtTokenValidator`, suggesting its purpose is to validate JWT tokens.
   - It's declared as a `public` class.

2. **Methods**:
   - **doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)**:
     - Overrides the `doFilterInternal` method from `OncePerRequestFilter`, which is called by the filter chain for each incoming HTTP request.
     - Retrieves the JWT token from the request's headers using the constant `JwtConstant.JWT_HEADER`.
     - If a JWT token is found:
       - Removes the "Bearer " prefix from the JWT token string.
       - Parses the JWT token to extract its claims, including email and authorities, using the secret key from `JwtConstant.SECRET_KEY`.
       - Constructs an authentication object (`Authentication`) based on the extracted email and authorities.
       - Sets the authentication object into the `SecurityContextHolder` to indicate that the user associated with the JWT token is authenticated.
     - If the JWT token is invalid or not present, it throws a `BadCredentialsException`.
     - Calls `filterChain.doFilter()` to proceed with the request processing chain.

3. **Purpose**:
   - `JwtTokenValidator` is responsible for validating JWT tokens extracted from incoming HTTP requests.
   - It extracts the JWT token from the request's headers, validates its authenticity and integrity using the secret key, and constructs an authentication object for the user associated with the token.
   - By setting the authentication object in the `SecurityContextHolder`, it enables Spring Security to handle authenticated user requests appropriately.

Overall, `JwtTokenValidator` plays a crucial role in ensuring the security of the application by validating JWT tokens and authenticating users based on those tokens. It integrates with Spring Security to enforce access control and authorization based on the provided JWT tokens.
* */