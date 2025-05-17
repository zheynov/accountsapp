package by.redlaw.acocuntsapp.controller;

import by.redlaw.accountsapp.api.AccountApi;
import by.redlaw.accountsapp.dto.MoneyTransferRequestDto;
import by.redlaw.acocuntsapp.service.AccountService;
import by.redlaw.acocuntsapp.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;
    private final AuthenticationService authenticationService;


    @Override
    public ResponseEntity<Void> accountsTransferPost(@RequestBody @Valid MoneyTransferRequestDto request) {
        Long fromUserId = authenticationService.getCurrentUserId();
        accountService.transferMoney(fromUserId, request.getToUserId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}
