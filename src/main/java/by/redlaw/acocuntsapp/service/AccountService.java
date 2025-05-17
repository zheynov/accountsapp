package by.redlaw.acocuntsapp.service;

import by.redlaw.acocuntsapp.db.entity.Account;
import by.redlaw.acocuntsapp.db.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public void save(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    @Scheduled(fixedRate = 30000)
    public void increaseAllUsersBalances() {
        accountRepository.increaseAllUsersBalances();
        log.info("All accounts are rebalanced");
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot transfer to yourself");
        }

        Account from = (fromUserId < toUserId) ? lockAccount(fromUserId) : lockAccount(toUserId);
        Account to = (fromUserId < toUserId) ? lockAccount(toUserId) : lockAccount(fromUserId);

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);

        log.info("Transfer: {} from userId={} to userId={}", amount, fromUserId, toUserId);
    }


    private Account lockAccount(Long userId) {
        return accountRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for userId: " + userId));
    }
}
