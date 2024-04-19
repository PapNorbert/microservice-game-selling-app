package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.model.Role;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.UserRepository;
import edu.ubb.consolegamesales.backend.service.exception.UsernameNotAvailableException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public User register(User requestUser) throws UsernameNotAvailableException {
        if (userRepository.findByUsername(requestUser.getUsername()).isPresent()) {
            throw new UsernameNotAvailableException("Username " + requestUser.getUsername() + " not available");
        }
        requestUser.setRegistrationDate(new Date());
        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        requestUser.setRole(Role.USER);
        return userRepository.saveAndFlush(requestUser);
    }

    public String authenticate(User requestUser) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword()));
        // if authentication didn't fail
        User user = userRepository.findByUsername(requestUser.getUsername())
                .orElseThrow(() -> new NotFoundException("User with username " + requestUser.getUsername() + " not found"));
        return jwtService.generateToken(user);

    }

}
