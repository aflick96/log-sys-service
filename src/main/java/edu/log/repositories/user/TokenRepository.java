/*
 * TokenRepository.java
 * 
 * This repository interface is used to manage Token entities in the database. It extends JpaRepository to provide CRUD operations and custom query methods for finding tokens by their value or associated user.
 */

package edu.log.repositories.user;

import edu.log.models.user.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenValue(String tokenValue);
    Optional<Token> findByUser(User user);
}
