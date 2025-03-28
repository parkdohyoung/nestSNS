package com.nest.repository;

import com.nest.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


    @Query("SELECT a FROM Account a WHERE a.verificationToken = :token")
    Optional<Account> findByVerificationToken(@Param("token")String token);

    boolean existsByEmail(String email);
    boolean existsByName(String name);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByName(String name);
}
