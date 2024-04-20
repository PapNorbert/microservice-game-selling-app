package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.model.Role;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.UserRepository;
import edu.ubb.consolegamesales.backend.service.exception.InvalidTokenException;
import edu.ubb.consolegamesales.backend.service.exception.RefreshUserNotFoundException;
import edu.ubb.consolegamesales.backend.service.exception.UsernameNotAvailableException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@AllArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final int ONE_HOUR = 60 * 60 * 1000;
    private static final int ONE_DAY = ONE_HOUR * 24;


    public User register(User requestUser) throws UsernameNotAvailableException {
        if (userRepository.findByUsername(requestUser.getUsername()).isPresent()) {
            throw new UsernameNotAvailableException("Username " + requestUser.getUsername() + " not available");
        }
        requestUser.setRegistrationDate(new Date());
        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        requestUser.setRole(Role.USER);
        requestUser.setRefreshToken(null);
        return userRepository.saveAndFlush(requestUser);
    }

    public Pair<String, String> authenticate(User requestUser) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword()));
        // if authentication didn't fail
        User user = userRepository.findByUsername(requestUser.getUsername())
                .orElseThrow(() -> new NotFoundException(
                        "User with username " + requestUser.getUsername() + " not found"));
        String accesToken = jwtService.generateToken(user, ONE_HOUR);
        String refreshToken = jwtService.generateToken(user, ONE_DAY);
        // refreshToken is saved in db, deleted on logout
        user.setRefreshToken(refreshToken);
        userRepository.update(user.getEntityId(), user);
        // refreshToken is used in cookie, accesToken used by client application
        return Pair.of(accesToken, refreshToken);

    }

    public String refresh(String token) {
        if (token == null) {
            throw new RefreshUserNotFoundException();
        }
        try {
            User user = userRepository.findByRefreshToken(token);
            if (user == null) {
                throw new RefreshUserNotFoundException();
            }
            if (jwtService.verifyToken(token, user)) {
                return jwtService.generateToken(user, ONE_HOUR);
            } else {
                throw new InvalidTokenException("Invalid token for refresh");
            }
        } catch (EntityNotFoundException e) {
            throw new RefreshUserNotFoundException();
        }
    }

}
