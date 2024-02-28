package com.tmsuserservice.controller;

import com.tmsuserservice.model.User;
import com.tmsuserservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
        User user = userService.getUserProfile(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUserProfile(){
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }
}

/*
* The `UserController` class is a REST controller responsible for handling HTTP requests related to user operations. Let's break down its functionality:

1. **Annotations**:
   - `@RestController`: Indicates that this class defines a REST controller, meaning it's responsible for handling HTTP requests and returning appropriate responses.
   - `@RequestMapping("api/users")`: Specifies the base URL path for all endpoints defined in this controller. All endpoints in this class will be relative to "/api/users".

2. **Dependencies**:
   - `@Autowired private UserService userService;`: Injects an instance of the `UserService` interface or its implementation into the controller to handle user-related business logic.

3. **Endpoints**:
   - **getUserProfile(@RequestHeader("Authorization") String jwt)**:
     - Annotated with `@GetMapping("/profile")`, meaning it handles HTTP GET requests at the "/api/users/profile" endpoint.
     - Retrieves the JWT token from the request header "Authorization".
     - Calls the `getUserProfile()` method of the `userService` to fetch the profile of the user associated with the provided JWT token.
     - Returns the user profile in the response body with an HTTP status of OK (200).

   - **getAllUserProfile()**:
     - Annotated with `@GetMapping()`, meaning it handles HTTP GET requests at the "/api/users" endpoint.
     - Calls the `getAllUser()` method of the `userService` to fetch all user profiles.
     - Returns a list of user profiles in the response body with an HTTP status of OK (200).

4. **Purpose**:
   - `UserController` acts as an interface between the client-side application and the `UserService`, providing endpoints to fetch user profiles.
   - It handles requests for both individual user profiles (`getUserProfile`) and all user profiles (`getAllUserProfile`).
   - By using Spring's annotations and dependency injection, it simplifies the implementation of RESTful services and promotes modularity and separation of concerns.

Overall, `UserController` provides endpoints for interacting with user data, facilitating communication between the client application and the backend service responsible for user management.
* */