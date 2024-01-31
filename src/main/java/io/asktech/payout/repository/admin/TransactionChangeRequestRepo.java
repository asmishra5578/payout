package io.asktech.payout.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.asktech.payout.modal.admin.TransactionChangeRequest;

@Repository
public interface TransactionChangeRequestRepo extends JpaRepository<TransactionChangeRequest, Long> {

}
