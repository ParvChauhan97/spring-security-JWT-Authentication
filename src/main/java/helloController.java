package com.spring.springSecurity;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class helloController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  JwtUtils jwtUtils;

  @GetMapping("/hello")
  public String SayHello() {
    return "Hello Security";
  }

  @PreAuthorize("hasRole('ADMIN')") 
  @GetMapping("/admin/hello")
  public String sayAdminHello() {
        return "Hello, admin";
    }

  @GetMapping("/user/hello")
  public String sayUserHello() {
     return "Hello, User";
  }

  @PostMapping("/signin")
  public String login(@RequestBody LoginRequest loginRequest) {

    Authentication authentication;
     try{
       authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
     } catch (AuthenticationException e) {
        e.printStackTrace();
            return "Could not Authenticate";
     }
  }

  SecurityContextHolder.getContext().setAuthentication(authentication);
  UserDetails userDetails = (UserDetails) authentication.getPrincipal();
  String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
  return jwtToken;
}
