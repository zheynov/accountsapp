package by.redlaw.acocuntsapp.db.repository;

import by.redlaw.acocuntsapp.db.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Modifying
    @Query(value = """
                UPDATE Account a
                SET a.balance =
                    CASE
                        WHEN a.balance * 1.1 > a.initialBalanceLimit
                        THEN a.initialBalanceLimit
                        ELSE a.balance * 1.1
                    END
                WHERE a.balance < a.initialBalanceLimit
            """)
    void increaseAllUsersBalances();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    Optional<Account> findByUserIdForUpdate(@Param("userId") Long userId);
    // есть вероятность эксепшнов под высокой нагрузкой, в этом случае можно было бы продумать механизм ретрая/таймаутов
}