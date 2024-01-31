package io.asktech.payout.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.asktech.payout.modal.admin.MerchantRecharge;

@Repository
public interface MerchantRechargeRepo extends JpaRepository<MerchantRecharge, String> {

    public MerchantRecharge findByUtrid(String urtid);

    public List<MerchantRecharge> findAllByMerchantId(String mid);

    @Query(value = "select mr.* from MerchantRecharge mr where "
            + "date(mr.created) between :dateFrom and :dateTo ", nativeQuery = true)
    public List<MerchantRecharge> getMerchantRechargeDateRange(
            @Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);
}
