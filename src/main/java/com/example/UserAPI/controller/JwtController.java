package com.example.UserAPI.controller;


import com.example.UserAPI.jwt.entity.JwtRequest;
import com.example.UserAPI.jwt.entity.JwtResponse;
import com.example.UserAPI.jwt.util.JwtUtil;
import com.example.UserAPI.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService customUserDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) throws Exception{

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
        }
        catch (BadCredentialsException badCredentialsException){
            throw  new Exception("Incorrect Username or Password");
        }
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
