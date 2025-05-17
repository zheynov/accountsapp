package by.redlaw.acocuntsapp.service;

import by.redlaw.acocuntsapp.db.entity.EmailData;
import by.redlaw.acocuntsapp.db.repository.EmailDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailDataService {
    private final EmailDataRepository emailDataRepository;

    public boolean existsByEmail(String email) {
        return emailDataRepository.existsByEmail(email);
    }

    public Optional<EmailData> findByUserIdAndEmail(Long userId, String email) {
        return emailDataRepository.findByUserIdAndEmail(userId, email);
    }

    public Optional<EmailData> findByEmail(String email) {
        return emailDataRepository.findByEmail(email);
    }

    public void save(EmailData emailData) {
        emailDataRepository.save(emailData);
    }

    public void delete(EmailData emailData) {
        emailDataRepository.delete(emailData);
    }

    public long countByUserId(Long userId) {
        return emailDataRepository.countByUserId(userId);
    }
}
