package io.asktech.payout.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.asktech.payout.wallet.modal.WalletTransactions;

public interface WalletTransactionsRepo extends JpaRepository<WalletTransactions, String> {

	@Query(value = "SELECT * FROM WalletTransactions  where referenceId = :referenceId and credit_debit = :credit_debit and transactionStatus = 'SUCCESS'", nativeQuery = true)
	public List<WalletTransactions> getByReferenceIdandCreditdebit(@Param("referenceId") String referenceId,
			@Param("credit_debit") String credit_debit);

	@Query(value = "SELECT * FROM WalletTransactions  where merchantid = :merchantId and date(created) = :createdFrom", nativeQuery = true)
	public List<WalletTransactions> getByCreatedAndMerchantId(@Param("createdFrom") String created,
			@Param("merchantId") String merchantid);

	@Query(value = "SELECT * FROM WalletTransactions  where merchantId = :merchantId and date(created) between :createdFrom and :createdTo", nativeQuery = true)
	public List<WalletTransactions> getByCreatedAndMerchantBetween(@Param("createdFrom") String createdFrom,
			@Param("createdTo") String createdTo, @Param("merchantId") String merchantId);

	@Query(value = "SELECT * FROM WalletTransactions  where date(created) between :createdFrom and :createdTo", nativeQuery = true)
	public List<WalletTransactions> getTransactionDateRange(String createdFrom, String createdTo);

	@Query(value = "SELECT * FROM WalletTransactions  where transactionStatus like :status and merchantId = :merchantid limit 5000", nativeQuery = true)
	List<WalletTransactions> findByTransactionStatusAndMerchantId(String status, String merchantid);

	@Query(value = "SELECT * FROM WalletTransactions  where transactionStatus like :transactionStatus and transactionType like :transactionType and merchantId = :merchantId ", nativeQuery = true)
	public List<WalletTransactions> getByStatusAndTypeAndId(@Param("transactionStatus") String transactionStatus,
			@Param("transactionType") String transactionType, @Param("merchantId") String merchantId);

	@Query(value = "SELECT * FROM WalletTransactions  where transactionType like :transactionType and merchantId = :merchantId limit 5000", nativeQuery = true)
	List<WalletTransactions> getSearchByMerchantIdAndTransactionType(@Param("transactionType") String transactionType,
			@Param("merchantId") String merchantId);

	@Query(value = "SELECT * FROM WalletTransactions  where merchantId = :merchantId order by id desc limit 5000", nativeQuery = true)
	List<WalletTransactions> getByMerchantId(@Param("merchantId") String merchantId);

	List<WalletTransactions> findByTransactionId(String transactionid);

	@Query(value = "SELECT * FROM WalletTransactions  where credit_debit = :crdb order by id desc limit 5000", nativeQuery = true)
	List<WalletTransactions> getByCreditDebit(String crdb);

	@Query(value = "SELECT * FROM WalletTransactions  where walletid =:walletid", nativeQuery = true)
	List<WalletTransactions> getByWalletid(String walletid);

	@Query(value = "select wt.* " + "from WalletTransactions wt " + "where"
			+ " date(wt.created) between :dateFrom and :dateTo and wt.credit_debit = :crdb", nativeQuery = true)
	public List<WalletTransactions> getByDateAndCreditDebit(String dateFrom, String dateTo, String crdb);

	@Query(value = "select wt.* " + "from WalletTransactions wt " + "where"
			+ " date(wt.created) between :dateFrom and :dateTo and wt.credit_debit = :crdb and wt.merchantId = :merchantId", nativeQuery = true)
	public List<WalletTransactions> getByDateAndCreditDebitAndMerchantId(String dateFrom, String dateTo, String crdb,
			String merchantId);

	List<WalletTransactions> findByTransactionStatus(String status);

	@Query(value = "select wt.* " + "from WalletTransactions wt " + "where"
			+ " wt.credit_debit = :crdb and wt.merchantId = :merchantId", nativeQuery = true)
	public List<WalletTransactions> getByCreditDebitAndMerchantId(String crdb, String merchantId);

	@Query(value = "SELECT * FROM WalletTransactions  where transactionType like :transactionType", nativeQuery = true)
	List<WalletTransactions> getSearchByTransactionType(@Param("transactionType") String transactionType);

	@Query(value = "SELECT * FROM WalletTransactions  where date(created) >= :createdFrom ", nativeQuery = true)
	public List<WalletTransactions> getByCreatedfrom(@Param("createdFrom") String created);

	@Query(value = "SELECT * FROM WalletTransactions  where date(created) <= :createdTo ", nativeQuery = true)
	public List<WalletTransactions> getTransactionDateTo(String createdTo);

	@Query(value = "select wt.* " + "from WalletTransactions wt " + "where"
			+ " date(wt.created) between :dateFrom and :dateTo and wt.transactionStatus = :status", nativeQuery = true)
	public List<WalletTransactions> getByDateAndTransactionStatus(@Param("dateFrom") String dateFrom,
			@Param("dateTo") String dateTo, @Param("status") String status);

	@Query(value = "select wt.* " + "from WalletTransactions wt " + "where"
			+ " date(wt.created) between :dateFrom and :dateTo and wt.transactionType like :transactionType", nativeQuery = true)
	public List<WalletTransactions> getByDateAndTransactionType(@Param("dateFrom") String dateFrom,
			@Param("dateTo") String dateTo, @Param("transactionType") String transactionType);

	@Query(value = "select wt.* " + "from WalletTransactions wt " + "where"
			+ " date(wt.created) >:dateFrom and wt.transactionType like :transactionType and wt.merchantId = :merchantId", nativeQuery = true)
	public List<WalletTransactions> getByDateFromAndTransactionType(String merchantId,
			@Param("dateFrom") String dateFrom, @Param("transactionType") String transactionType);

	@Query(value = "select wt.* " + "from WalletTransactions wt " + "where"
			+ " date(wt.created) between :dateFrom and :dateTo and wt.transactionType= :transactionType and wt.merchantId = :merchantId", nativeQuery = true)
	public List<WalletTransactions> getByDateAndTransactionTypePay(String merchantId,
			@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo,
			@Param("transactionType") String transactionType);

	// @Query(value = "select wt.*"
	// + " from WalletTransactions tr where wt.merchantId = :merchantId and
	// date(wt.created) >=DATE_ADD(CURDATE(), INTERVAL -7 DAY)",
	// nativeQuery = true)
	// public List<WalletTransactions> findLast7DaysTransaction(String merchantId);

}
