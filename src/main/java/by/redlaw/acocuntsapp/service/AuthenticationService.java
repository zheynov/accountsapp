package by.redlaw.acocuntsapp.service;

import by.redlaw.accountsapp.dto.AuthenticationRequestDto;
import by.redlaw.accountsapp.dto.AuthenticationResponseDto;
import by.redlaw.accountsapp.dto.RegistrationRequestDto;
import by.redlaw.acocuntsapp.db.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;
    private final EmailDataService emailDataService;
    private final PhoneDataService phoneDataService;
    private final AccountService accountService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationResponseDto register(RegistrationRequestDto request) {
        if (request.getEmail() != null && emailDataService.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with such email already exist.");
        }

        if (request.getPhone() != null && phoneDataService.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("User with such phone number already exist.");
        }

        var user = User.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .dateOfBirth(request.getDateOfBirth() != null ? request.getDateOfBirth().toLocalDate() : null)
                .role(Role.USER)
                .enabled(true)
                .build();

        userDetailsService.saveUser(user);

        if (request.getEmail() != null) {
            emailDataService.save(new EmailData(null, user, request.getEmail()));
        }

        if (request.getPhone() != null) {
            phoneDataService.save(new PhoneData(null, user, request.getPhone()));
        }

        accountService.save(new Account(null, user, BigDecimal.ZERO, BigDecimal.ZERO));


        String token = jwtService.generateToken(user.getId());

        return new AuthenticationResponseDto(token);
    }

    public AuthenticationResponseDto login(AuthenticationRequestDto request) {
        String login = Optional.ofNullable(request.getEmail()).orElse(request.getPhone());
        if (login == null) {
            throw new IllegalArgumentException("Email or phone must be provided");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, request.getPassword()));

        } catch (Exception ex) {
            log.warn("Authentication failed for login: {}", login);
            throw ex;
        }


        User user = userDetailsService.loadUserByUsername(login);

        String token = jwtService.createToken(user.getId());

        log.info("Token issued for userId={}", user.getId());

        return new AuthenticationResponseDto(token);
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Long)) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        Long userId = (Long) authentication.getPrincipal();
        return userId;
    }
}
