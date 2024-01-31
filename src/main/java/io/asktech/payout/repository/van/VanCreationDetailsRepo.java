package io.asktech.payout.repository.van;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.asktech.payout.dataInterfaces.IMaxAcc;
import io.asktech.payout.modal.van.VanCreationDetails;


public interface VanCreationDetailsRepo extends JpaRepository<VanCreationDetails, String>{
	
	@Query(value = "SELECT COALESCE(MAX(sequenceNo), 0) + 1 accno FROM VanCreationDetails",
			nativeQuery = true)
	public IMaxAcc getMaxAccno();

	public VanCreationDetails findByReferenceId(String id);

	public VanCreationDetails findByMerchantId(String merchantID);
}
