package by.redlaw.acocuntsapp.db.repository.spec;

import by.redlaw.acocuntsapp.db.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecifications {

    public static Specification<User> withFilters(String name, LocalDate dateOfBirth, String email, String phone) {
        return (root, query, cb) -> {
            query.distinct(true);

            Predicate predicate = cb.conjunction();

            if (name != null && !name.isBlank()) {
                predicate = cb.and(predicate, cb.like(root.get("name"), name + "%"));
            }

            if (dateOfBirth != null) {
                predicate = cb.and(predicate, cb.greaterThan(root.get("dateOfBirth"), dateOfBirth));
            }

            if (email != null && !email.isBlank()) {
                Join<Object, Object> emailsJoin = root.join("emails");
                predicate = cb.and(predicate, cb.equal(emailsJoin.get("email"), email));
            }

            if (phone != null && !phone.isBlank()) {
                Join<Object, Object> phonesJoin = root.join("phones");
                predicate = cb.and(predicate, cb.equal(phonesJoin.get("phone"), phone));
            }

            return predicate;
        };
    }
}
