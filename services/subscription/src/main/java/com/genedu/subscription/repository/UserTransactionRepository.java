package com.genedu.subscription.repository;

import com.genedu.subscription.model.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, UUID> {
    // Define custom query methods if needed
}
