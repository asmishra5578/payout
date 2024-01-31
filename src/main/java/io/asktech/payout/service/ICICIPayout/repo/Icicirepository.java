package io.asktech.payout.service.ICICIPayout.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.ICICIPayout.model.IcicipayoutTransactiondetails;

public interface Icicirepository extends JpaRepository<IcicipayoutTransactiondetails, String> {
    IcicipayoutTransactiondetails findByOrderId(String orderid);
}
