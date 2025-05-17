package by.redlaw.acocuntsapp.db.repository;

import by.redlaw.acocuntsapp.db.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
    Optional<PhoneData> findByPhone(String phone);

    boolean existsByPhone(String phone);

    Optional<PhoneData> findByUserIdAndPhone(Long userId, String phone);

    long countByUserId(Long userId);

}