package by.redlaw.acocuntsapp.controller;

import by.redlaw.accountsapp.api.UserApi;
import by.redlaw.accountsapp.dto.*;
import by.redlaw.acocuntsapp.db.entity.EmailData;
import by.redlaw.acocuntsapp.db.entity.PhoneData;
import by.redlaw.acocuntsapp.service.AuthenticationService;
import by.redlaw.acocuntsapp.service.UserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserDetailsService userDetailsService;
    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<PageUserSearchResponseDto> apiUserSearchGet(String name, String phone, String email, LocalDateTime dateOfBirth, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(20)
        );

        LocalDate localDate = dateOfBirth != null ? dateOfBirth.toLocalDate() : null;
        Page<UserSearchResponseDto> result = userDetailsService.searchUsers(name, phone, email, localDate, pageable);

        PageUserSearchResponseDto response = new PageUserSearchResponseDto();
        response.setContent(result.getContent());
        response.setPage(result.getNumber());
        response.setSize(result.getSize());
        response.setTotalElements((int) result.getTotalElements());
        response.setTotalPages(result.getTotalPages());

        log.info("User search with filters: name={}, email={}, phone={}, dateOfBirth={}", name, email, phone, dateOfBirth);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<EmailResponseDto> apiUserEmailPost(@Valid EmailRequestDto emailRequestDto) {
        Long userId = authenticationService.getCurrentUserId();
        EmailData emailData = userDetailsService.addEmail(userId, emailRequestDto.getEmail());
        return ResponseEntity.ok(new EmailResponseDto(emailData.getId(), emailData.getEmail()));
    }

    @Override
    public ResponseEntity<EmailResponseDto> apiUserEmailPut(@Valid UpdateEmailRequestDto updateEmailRequestDto) {
        Long userId = authenticationService.getCurrentUserId();
        EmailData emailData = userDetailsService.updateEmail(userId, updateEmailRequestDto.getOldEmail(), updateEmailRequestDto.getNewEmail());
        return ResponseEntity.ok(new EmailResponseDto(emailData.getId(), emailData.getEmail()));
    }

    @Override
    public ResponseEntity<Void> apiUserEmailDelete(String email) {
        Long userId = authenticationService.getCurrentUserId();
        userDetailsService.deleteEmail(userId, email);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PhoneResponseDto> apiUserPhonePost(@Valid PhoneRequestDto phoneRequestDto) {
        Long userId = authenticationService.getCurrentUserId();
        PhoneData phoneData = userDetailsService.addPhone(userId, phoneRequestDto.getPhone());
        return ResponseEntity.ok(new PhoneResponseDto(phoneData.getId(), phoneData.getPhone()));
    }

    @Override
    public ResponseEntity<PhoneResponseDto> apiUserPhonePut(@Valid UpdatePhoneRequestDto updatePhoneRequestDto) {
        Long userId = authenticationService.getCurrentUserId();
        PhoneData phoneData = userDetailsService.updatePhone(userId, updatePhoneRequestDto.getOldPhone(), updatePhoneRequestDto.getNewPhone());
        return ResponseEntity.ok(new PhoneResponseDto(phoneData.getId(), phoneData.getPhone()));
    }

    @Override
    public ResponseEntity<Void> apiUserPhoneDelete(String phone) {
        Long userId = authenticationService.getCurrentUserId();
        userDetailsService.deletePhone(userId, phone);
        return ResponseEntity.ok().build();
    }


}