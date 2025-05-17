package by.redlaw.acocuntsapp.db.repository;

import by.redlaw.acocuntsapp.db.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {

    Optional<EmailData> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<EmailData> findByUserIdAndEmail(Long userId, String email);

    long countByUserId(Long userId);
}