package io.asktech.payout.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.wallet.modal.CommissionsDeduction;

public interface CommissionsDeductionRepo extends JpaRepository<CommissionsDeduction, String> {
	List<CommissionsDeduction> findByGateway(String gateway);

	List<CommissionsDeduction> findByGatewayAndType(String gateway, String type);
}
