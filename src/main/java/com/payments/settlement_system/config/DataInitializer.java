package com.payments.settlement_system.config;

import com.payments.settlement_system.model.UserAccount;
import com.payments.settlement_system.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;

//@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;

    /**
     * This method will be executed automatically by Spring Boot after the
     * application context has been loaded.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing database with sample users...");

        // Create User A
        UserAccount userA = new UserAccount();
        userA.setUsername("A");
        userA.setBalance(new BigDecimal("2000.00"));

        // Create User B
        UserAccount userB = new UserAccount();
        userB.setUsername("B");
        userB.setBalance(new BigDecimal("500.00"));

        // Create User C
        UserAccount userC = new UserAccount();
        userC.setUsername("C");
        userC.setBalance(new BigDecimal("1000.00"));

        // Create User D
        UserAccount userD = new UserAccount();
        userD.setUsername("D");
        userD.setBalance(new BigDecimal("1500.00"));

        // Save all users to the database using the repository
        userAccountRepository.save(userA);
        userAccountRepository.save(userB);
        userAccountRepository.save(userC);
        userAccountRepository.save(userD);

        System.out.println("Database initialization complete.");
        System.out.println("Current user balances:");
        userAccountRepository.findAll().forEach(user ->
                System.out.println("- User: " + user.getUsername() + ", Balance: " + user.getBalance())
        );
    }
}
