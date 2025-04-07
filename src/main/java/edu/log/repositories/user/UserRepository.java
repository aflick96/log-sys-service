/*
 * UserRepository.java
 * 
 * This interface defines the repository for the User entity. It extends JpaRepository to provide CRUD operations and custom methods to find a user by email and check if an email exists.
 */

package edu.log.repositories.user;

import edu.log.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
