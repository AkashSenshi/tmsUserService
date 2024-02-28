package com.tmsuserservice.controller;

import com.tmsuserservice.config.JwtProvider;
import com.tmsuserservice.model.User;
import com.tmsuserservice.repository.UserRepository;
import com.tmsuserservice.request.LoginRequest;
import com.tmsuserservice.response.AuthResponse;
import com.tmsuserservice.service.CustomerUserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerUserServiceImplementation customUserServiceImplementation;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception{
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String role = user.getRole();

        User isEmailExist = userRepository.findByEmail(email);
        if(isEmailExist!=null){
            throw new Exception("Email Is Already Used With Another Account");
        }

        User createdUser = new User();
        createdUser.setEmail(email);;
        createdUser.setFullName(fullName);;
        createdUser.setRole(role);;
        createdUser.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse>  signin(@RequestBody LoginRequest loginRequest){

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login Success");
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        return  new ResponseEntity<>(authResponse,HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("Invalid username or Password");
        }

        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            System.out.println("Sign in userDetails - password mismatches "+ userDetails);
            throw new BadCredentialsException("Invalid username or Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }


    /*
    * This `AuthController` class is responsible for handling user authentication-related endpoints in a Spring Boot application. Let's break down the analysis:

1. **Annotations**:
   - `@RestController`: Indicates that this class defines a REST controller. It's responsible for handling incoming HTTP requests.
   - `@RequestMapping("/auth")`: Specifies the base URL path for all endpoints defined in this controller.

2. **Autowired Dependencies**:
   - `UserRepository`: This dependency is presumably a Spring Data JPA repository for managing `User` entities.
   - `PasswordEncoder`: Used to encode passwords securely.
   - `CustomerUserServiceImplementation`: This seems to be a custom implementation of Spring Security's `UserDetailsService`, used for loading user details during authentication.

3. **Endpoints**:
   - `@PostMapping("/signup")`: Defines an endpoint for user registration/signup.
   - `@PostMapping("/signin")`: Defines an endpoint for user authentication/sign-in.

4. **createUserHandler Method**:
   - Handles user registration/signup requests.
   - Checks if the provided email already exists in the database.
   - If the email is not found, creates a new `User` object, encodes the password, and saves it to the database.
   - Generates a JWT token for the newly registered user and sends it in the response.

5. **signin Method**:
   - Handles user authentication/sign-in requests.
   - Authenticates the user by calling the `authenticate` method.
   - Generates a JWT token for the authenticated user and sends it in the response.

6. **authenticate Method**:
   - Performs authentication by loading user details using the `UserDetailsService` implementation.
   - Checks if the provided username exists and if the password matches the encoded password stored in the database.
   - If authentication fails, it throws a `BadCredentialsException`.

Overall, this `AuthController` provides basic authentication functionality for user registration and sign-in using JWT tokens, integrating with a `UserRepository` for data access and a `PasswordEncoder` for secure password storage and verification.
    *
    * */
}
