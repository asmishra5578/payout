package io.asktech.payout.repository.van;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.van.PayTmVanUpdateResonse;

public interface PayTmVanUpdateResRepo extends JpaRepository<PayTmVanUpdateResonse, String>{

}
