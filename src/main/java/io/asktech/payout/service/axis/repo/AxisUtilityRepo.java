package io.asktech.payout.service.axis.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.axis.model.AxisUtilityModel;

// import io.asktech.payout.service.axis.AxisUtility;
// import io.asktech.payout.service.axis.model.AxisBeneficiary;

public interface AxisUtilityRepo extends JpaRepository<AxisUtilityModel, String> {

    AxisUtilityModel findByBeneCode(String beneCode);
    AxisUtilityModel findByOrderId(String orderId);

}
