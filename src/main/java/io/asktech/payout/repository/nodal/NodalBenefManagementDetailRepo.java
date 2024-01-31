package io.asktech.payout.repository.nodal;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.nodal.NodalBenefManagementDetailModal;

public interface NodalBenefManagementDetailRepo extends JpaRepository<NodalBenefManagementDetailModal, String>{

	NodalBenefManagementDetailModal findByMerchantIdAndBeneficiaryNickName(String merchantId,
			String beneficiaryNickName);

	NodalBenefManagementDetailModal findByResponse(String beneficiaryRefId);

}