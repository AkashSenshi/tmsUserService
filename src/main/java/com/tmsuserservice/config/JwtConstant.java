package com.tmsuserservice.config;

public class JwtConstant {
    public static final String SECRET_KEY = "dvjshvdashvfhasvcvjhasbcjkbasjkcbjhasvdjvsxvhZxhshxvsgvxcvcgcvzvcgasgxaSVXJHsvj";
    public static final String JWT_HEADER = "Authorization";

}


/*
* The `JwtConstant` class contains constant values related to JWT (JSON Web Token) authentication. Here's the analysis:

1. **Class Declaration**:
   - Declared as `public`, indicating that it can be accessed from outside its package.
   - Named `JwtConstant`, suggesting its purpose is to hold constants related to JWT.

2. **Constants**:
   - **SECRET_KEY**:
     - Represents the secret key used for JWT signing and verification.
     - It's declared as `public static final`, indicating that it's a constant value that can be accessed without creating an instance of the class. It cannot be modified.
     - The value is a long string of characters, typically used as the secret key for JWT encryption and decryption.

   - **JWT_HEADER**:
     - Represents the name of the HTTP header used to transmit JWT tokens.
     - It's also declared as `public static final`.
     - The value is "Authorization", which is a common convention for JWT tokens passed in the authorization header of HTTP requests.

3. **Purpose**:
   - By centralizing these constants in the `JwtConstant` class, it provides a single location to manage JWT-related configurations and ensures consistency throughout the application.
   - Developers can easily reference these constants in their code without hardcoding string values, promoting maintainability and reducing the risk of errors.

Overall, `JwtConstant` serves as a convenient container for storing JWT-related constant values, such as the secret key and the JWT header name, improving code readability and maintainability.
*
* */