package com.payments.settlement_system.repository;

import com.payments.settlement_system.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Spring Data JPA repository for the UserAccount entity.
 * This interface handles all the database operations (CRUD) for UserAccount objects.
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
