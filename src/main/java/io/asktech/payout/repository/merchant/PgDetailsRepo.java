package io.asktech.payout.repository.merchant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.asktech.payout.modal.merchant.PgDetails;

public interface PgDetailsRepo extends JpaRepository<PgDetails, String>{
	
    PgDetails findByPgIdAndPgStatus(String pgId, String status);
    PgDetails findByPgId(String pgId);
    PgDetails findByPgName(String pgName);
    
    @Query(value = "SELECT * FROM payout.PgDetails WHERE pgId = :pgId AND pgName = :pgName", nativeQuery = true)
    PgDetails findByPgIdAndPgName(String pgId, String pgName);
}
