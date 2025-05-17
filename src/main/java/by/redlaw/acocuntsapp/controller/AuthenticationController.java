package by.redlaw.acocuntsapp.controller;


import by.redlaw.accountsapp.api.AuthApi;
import by.redlaw.accountsapp.dto.AuthenticationRequestDto;
import by.redlaw.accountsapp.dto.AuthenticationResponseDto;
import by.redlaw.accountsapp.dto.RegistrationRequestDto;
import by.redlaw.acocuntsapp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthApi {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<AuthenticationResponseDto> apiAuthLoginPost(AuthenticationRequestDto request) {
        AuthenticationResponseDto response = authenticationService.login(request);
        String login = request.getEmail() != null ? request.getEmail() : request.getPhone();
        log.info("Login successful for user: {}***", login.substring(0, 3));
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<AuthenticationResponseDto> apiAuthRegisterPost(RegistrationRequestDto request) {
        if ((request.getEmail() == null && request.getPhone() == null) ||
            (request.getEmail() != null && request.getPhone() != null)) {
            throw new IllegalArgumentException("Enter email or phone.");
        }

        AuthenticationResponseDto response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }


}