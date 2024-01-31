package io.asktech.payout.service.axis.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.axis.model.AxisBeneficiary;



public interface AxisBeneficaryRepo extends JpaRepository<AxisBeneficiary, String> {
    AxisBeneficiary findByBeneCode(String beneCode);
    AxisBeneficiary findByBeneBankAccount(String beneBankAccount);
}
