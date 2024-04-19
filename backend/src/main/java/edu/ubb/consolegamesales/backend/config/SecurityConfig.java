package edu.ubb.consolegamesales.backend.config;

import edu.ubb.consolegamesales.backend.filter.AuthenticationFilter;
import edu.ubb.consolegamesales.backend.service.security.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImp userDetailsServiceImp;

    private final AuthenticationFilter authenticationFilter;

    private final String apiPrefix;


    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp, AuthenticationFilter authenticationFilter,
                          @Value("${apiPrefix}") String apiPrefix) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.authenticationFilter = authenticationFilter;
        this.apiPrefix = apiPrefix;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/" + apiPrefix + "/login/**", "/" + apiPrefix + "/register/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/" + apiPrefix + "/announcements").permitAll()
                                .anyRequest().authenticated())
                .userDetailsService(userDetailsServiceImp)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

