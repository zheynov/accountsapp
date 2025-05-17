package by.redlaw.acocuntsapp.unit;


import by.redlaw.acocuntsapp.db.entity.Account;
import by.redlaw.acocuntsapp.db.entity.User;
import by.redlaw.acocuntsapp.db.repository.AccountRepository;
import by.redlaw.acocuntsapp.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final Long FROM_USER_ID = 1L;
    private static final Long TO_USER_ID = 2L;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldTransferMoneyWithValidValues() {
        BigDecimal amount = new BigDecimal("100");

        Account fromAccount = createAccount(FROM_USER_ID);
        fromAccount.setBalance(new BigDecimal("200"));

        Account toAccount = createAccount(TO_USER_ID);
        toAccount.setBalance(new BigDecimal("50"));

        when(accountRepository.findByUserIdForUpdate(FROM_USER_ID)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(TO_USER_ID)).thenReturn(Optional.of(toAccount));

        accountService.transferMoney(FROM_USER_ID, TO_USER_ID, amount);

        assertEquals(new BigDecimal("100"), fromAccount.getBalance());
        assertEquals(new BigDecimal("150"), toAccount.getBalance());

        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
    }


    @Test
    void shouldFailTransferWhenNotEnoughMoney() {
        BigDecimal amount = new BigDecimal("300");

        Account fromAccount = createAccount(FROM_USER_ID);
        fromAccount.setBalance(new BigDecimal("200"));

        Account toAccount = createAccount(TO_USER_ID);
        toAccount.setBalance(new BigDecimal("50"));

        when(accountRepository.findByUserIdForUpdate(FROM_USER_ID)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(TO_USER_ID)).thenReturn(Optional.of(toAccount));

        assertThrows(IllegalArgumentException.class, () ->
                accountService.transferMoney(FROM_USER_ID, TO_USER_ID, amount)
        );

        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldFailTransferForSameUser() {
        assertThrows(IllegalArgumentException.class, () ->
                accountService.transferMoney(1L, 1L, new BigDecimal("50"))
        );
    }

    @Test
    void shouldFailTransferWhenAccountNotFound() {
        when(accountRepository.findByUserIdForUpdate(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                accountService.transferMoney(1L, 2L, new BigDecimal("10"))
        );
    }

    private static @NotNull Account createAccount(Long fromUserId) {
        User fromUser = new User();
        fromUser.setId(fromUserId);
        Account fromAccount = new Account();
        fromAccount.setUser(fromUser);
        return fromAccount;
    }
}