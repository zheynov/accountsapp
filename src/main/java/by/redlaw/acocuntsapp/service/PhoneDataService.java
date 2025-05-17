package by.redlaw.acocuntsapp.service;

import by.redlaw.acocuntsapp.db.entity.PhoneData;
import by.redlaw.acocuntsapp.db.repository.PhoneDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhoneDataService {
    private final PhoneDataRepository phoneDataRepository;

    public boolean existsByPhone(String phone) {
        return phoneDataRepository.existsByPhone(phone);
    }

    public Optional<PhoneData> findByPhone(String phone) {
        return phoneDataRepository.findByPhone(phone);
    }

    public Optional<PhoneData> findByUserIdAndPhone(Long userId, String phone) {
        return phoneDataRepository.findByUserIdAndPhone(userId, phone);
    }

    public void save(PhoneData phoneData) {
        phoneDataRepository.save(phoneData);
    }

    public void delete(PhoneData phoneData) {
        phoneDataRepository.delete(phoneData);
    }

    public long countByUserId(Long userId) {
        return phoneDataRepository.countByUserId(userId);
    }
}
