package by.redlaw.acocuntsapp.service;

import by.redlaw.accountsapp.dto.UserSearchResponseDto;
import by.redlaw.acocuntsapp.db.entity.EmailData;
import by.redlaw.acocuntsapp.db.entity.PhoneData;
import by.redlaw.acocuntsapp.db.entity.User;
import by.redlaw.acocuntsapp.db.repository.UserRepository;
import by.redlaw.acocuntsapp.db.repository.spec.UserSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final EmailDataService emailDataService;
    private final PhoneDataService phoneDataService;


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if (username.contains("@")) {
            user = emailDataService.findByEmail(username)
                    .map(EmailData::getUser)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found by email: " + username));

        } else {
            user = phoneDataService.findByPhone(username)
                    .map(PhoneData::getUser)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found by phone: " + username));
        }
        return user;

    }

    @Cacheable(
            cacheNames = "userSearchCache",
            key = "#name + '_' + #email + '_' + #phone + '_' + #dateOfBirth + '_' + #pageable.pageNumber + '_' + #pageable.pageSize",
            unless = "#result == null"
    )
    public Page<UserSearchResponseDto> searchUsers(String name, String phone, String email, LocalDate dateOfBirth,
                                                   Pageable pageable) {
        Specification<User> spec = UserSpecifications.withFilters(name, dateOfBirth, email, phone);
        Page<User> usersPage = userRepository.findAll(spec, pageable);


        log.debug("User search: name={}, page={}, size={}", name, pageable.getPageNumber(), pageable.getPageSize());

        return usersPage.map(user -> {
            List<String> emails = user.getEmails().stream()
                    .map(EmailData::getEmail)
                    .toList();

            List<String> phones = user.getPhones().stream()
                    .map(PhoneData::getPhone)
                    .toList();

            UserSearchResponseDto dto = new UserSearchResponseDto();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setDateOfBirth(user.getDateOfBirth().atStartOfDay());
            dto.setEmails(emails);
            dto.setPhones(phones);
            return dto;
        });
    }


    @CacheEvict(cacheNames = "userSearchCache", allEntries = true)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(cacheNames = "userSearchCache", allEntries = true)
    public EmailData addEmail(Long userId, String email) {
        if (emailDataService.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        EmailData emailData = new EmailData();
        emailData.setUser(user);
        emailData.setEmail(email);
        emailDataService.save(emailData);

        return emailData;
    }

    @CacheEvict(cacheNames = "userSearchCache", allEntries = true)
    public EmailData updateEmail(Long userId, String oldEmail, String newEmail) {
        if (emailDataService.existsByEmail(newEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "New email already in use");
        }

        EmailData emailData = emailDataService.findByUserIdAndEmail(userId, oldEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Old email not found"));

        emailData.setEmail(newEmail);
        emailDataService.save(emailData);
        return emailData;
    }

    @CacheEvict(cacheNames = "userSearchCache", allEntries = true)
    public void deleteEmail(Long userId, String email) {
        EmailData emailData = emailDataService.findByUserIdAndEmail(userId, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        if (emailDataService.countByUserId(userId) <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one email must remain in DB");
        }

        emailDataService.delete(emailData);
    }

    @CacheEvict(cacheNames = "userSearchCache", allEntries = true)
    public PhoneData addPhone(Long userId, String phone) {
        if (phoneDataService.existsByPhone(phone)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        PhoneData phoneData = new PhoneData();
        phoneData.setUser(user);
        phoneData.setPhone(phone);
        phoneDataService.save(phoneData);
        return phoneData;
    }

    @CacheEvict(cacheNames = "userSearchCache", allEntries = true)
    public PhoneData updatePhone(Long userId, String oldPhone, String newPhone) {
        if (phoneDataService.existsByPhone(newPhone)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "New phone already in use");
        }

        PhoneData phoneData = phoneDataService.findByUserIdAndPhone(userId, oldPhone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Old phone not found"));

        phoneData.setPhone(newPhone);
        phoneDataService.save(phoneData);
        return phoneData;
    }

    @CacheEvict(cacheNames = "userSearchCache", allEntries = true)
    public void deletePhone(Long userId, String phone) {
        PhoneData phoneData = phoneDataService.findByUserIdAndPhone(userId, phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone not found"));

        if (phoneDataService.countByUserId(userId) <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one phone must remain in DB");
        }

        phoneDataService.delete(phoneData);
    }

}