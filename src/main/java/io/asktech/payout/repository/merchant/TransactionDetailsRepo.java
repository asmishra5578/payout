package io.asktech.payout.repository.merchant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.asktech.payout.dto.admin.IHourandCountStatus;
import io.asktech.payout.dto.admin.IHourandStatusWise;
import io.asktech.payout.dto.admin.ILastTrxMercList;
import io.asktech.payout.dto.admin.IMerchantWisePgWiseSum;
import io.asktech.payout.dto.admin.IMinuteandCountStatus;
import io.asktech.payout.dto.admin.IPgTypeAndCountByStatusAndDate;
import io.asktech.payout.dto.admin.IStatusCountTrx;
import io.asktech.payout.dto.admin.StatusAndMinute;
import io.asktech.payout.modal.merchant.TransactionDetails;

public interface TransactionDetailsRepo extends JpaRepository<TransactionDetails, String> {

	TransactionDetails findByOrderId(String orderId);

	List<TransactionDetails> findByOrderIdAndMerchantId(String orderId, String merchantid);

	List<TransactionDetails> findByTransactionStatusAndMerchantId(String status, String merchantid);

	List<TransactionDetails> findByTransactionStatus(String status);

	@Query(value = "select tr.* " + "from TransactionDetails tr " + "where"
			+ " tr.bankaccount = :bankaccount order by sno desc limit 5000", nativeQuery = true)
	List<TransactionDetails> findByBankaccount(String bankaccount);

	@Query(value = "select tr.* " + "from TransactionDetails tr " + "where"
			+ " tr.beneficiaryName = :bankName order by sno desc limit 5000", nativeQuery = true)
	List<TransactionDetails> findByBeneficiaryName(String bankName);

	@Query(value = "select tr.* " + "from TransactionDetails tr " + "where"
			+ " tr.ifsc = :ifsc order by sno desc limit 5000", nativeQuery = true)
	List<TransactionDetails> findByIfsc(String ifsc);

	List<TransactionDetails> findByBankaccountAndBeneficiaryNameAndIfsc(String bankaccount, String bankName,
			String ifsc);

	List<TransactionDetails> findByBankaccountAndBeneficiaryName(String bankaccount, String bankName);

	List<TransactionDetails> findByBeneficiaryNameAndIfsc(String bankName, String ifsc);

	@Query(value = "select tr.* " + "from TransactionDetails tr " + "where tr.merchantId= :merchant_id "
			+ "and date(tr.created) = :dateFrom", nativeQuery = true)
	List<TransactionDetails> findTransactionDate(@Param("merchant_id") String merchantid,
			@Param("dateFrom") String dateFrom);

	@Query(value = "select tr.* " + "from TransactionDetails tr " + "where tr.merchantId= :merchant_id "
			+ "and date(tr.created) = :dateFrom  and status = :status", nativeQuery = true)
	public List<TransactionDetails> getTransactionDateAndTransactionStatus(@Param("merchant_id") String merchant_id,
			@Param("dateFrom") String dateFrom, @Param("status") String status);

	@Query(value = "select tr.* " + "from TransactionDetails tr "
			+ "where  date(tr.created) >= :dateFrom", nativeQuery = true)
	public List<TransactionDetails> getTransactionDateFrom(@Param("dateFrom") String dateFrom);

	@Query(value = "select tr.* " + "from TransactionDetails tr "
			+ "where  date(tr.created) <= :dateTo", nativeQuery = true)
	public List<TransactionDetails> getTransactionDateTo(@Param("dateTo") String dateTo);

	@Query(value = "select tr.* " + "from TransactionDetails tr " + "where tr.merchantId= :merchant_id "
			+ "and date(tr.created) between :dateFrom and :dateTo and status = :status", nativeQuery = true)
	public List<TransactionDetails> getTransactionDateRangeAndTransactionStatus(
			@Param("merchant_id") String merchant_id,
			@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo, @Param("status") String status);

	@Query(value = "select tr.* " + "from TransactionDetails tr " + "where"
			+ " date(tr.created) between :dateFrom and :dateTo and tr.transactionStatus = :status", nativeQuery = true)
	public List<TransactionDetails> getByDateAndTransactionStatus(@Param("dateFrom") String dateFrom,
			@Param("dateTo") String dateTo, @Param("status") String status);

	@Query(value = "select tr.* " + "from TransactionDetails tr " + "where"
			+ " date(tr.created) between :dateFrom and :dateTo and tr.bankaccount = :bankaccount", nativeQuery = true)
	public List<TransactionDetails> getByDateAndBankAccount(@Param("dateFrom") String dateFrom,
			@Param("dateTo") String dateTo, @Param("bankaccount") String bankaccount);

	@Query(value = "select tr.* " + "from TransactionDetails tr " + "where"
			+ " date(tr.created) between :dateFrom and :dateTo and tr.transactionType like :transactionType", nativeQuery = true)
	public List<TransactionDetails> getByDateAndTransactionType(@Param("dateFrom") String dateFrom,
			@Param("dateTo") String dateTo, @Param("transactionType") String transactionType);

	@Query(value = "select tr.* " + "from TransactionDetails tr "
			+ "where date(tr.created) between :dateFrom and :dateTo", nativeQuery = true)
	public List<TransactionDetails> getTransactionDateRange(@Param("dateFrom") String dateFrom,
			@Param("dateTo") String dateTo);

	@Query(value = "select tr.* " + "from TransactionDetails tr " + "where tr.merchantId= :merchant_id "
			+ "and date(tr.created) between :dateFrom and :dateTo ", nativeQuery = true)
	public List<TransactionDetails> getTransactionDateRange(@Param("merchant_id") String merchant_id,
			@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);

	@Query(value = "select * from TransactionDetails tr "
			+ "where created between (NOW() - INTERVAL 14 MINUTE) and (NOW() - INTERVAL 3 MINUTE) "
			+ "and transactionStatus = 'PENDING' and internalOrderId is not null "
			+ "and (reconStatus <>'R0' or reconStatus is null) order by sno", nativeQuery = true)
	List<TransactionDetails> getAllRecordsForPayout5Minutes();

	@Query(value = "select * from TransactionDetails tr "
			+ "where created between (NOW() - INTERVAL 20 MINUTE) and (NOW() - INTERVAL 10 MINUTE) "
			+ "and transactionStatus = 'PENDING' and internalOrderId is not null "
			+ "and (reconStatus <>'R1' or reconStatus is null) order by sno", nativeQuery = true)
	List<TransactionDetails> getAllRecordsForPayout15Minutes();

	@Query(value = "select * from TransactionDetails tr "
			+ "where created between (NOW() - INTERVAL 60 MINUTE) and (NOW() - INTERVAL 20 MINUTE) "
			+ "and transactionStatus = 'PENDING' and internalOrderId is not null "
			+ "and (reconStatus <>'R2' or reconStatus is null) order by sno", nativeQuery = true)
	List<TransactionDetails> getAllRecordsForPayout1Hours();

	@Query(value = "select * from TransactionDetails tr "
			+ "where created between (NOW() - INTERVAL 180 MINUTE) and (NOW() - INTERVAL 60 MINUTE) "
			+ "and transactionStatus = 'PENDING' and internalOrderId is not null "
			+ "and (reconStatus <>'R3' or reconStatus is null) order by sno", nativeQuery = true)
	List<TransactionDetails> getAllRecordsForPayout3Hours();

	@Query(value = "select * from TransactionDetails tr "
			+ "where created between (NOW() - INTERVAL 1440 MINUTE) and (NOW() - INTERVAL 720 MINUTE) "
			+ "and transactionStatus = 'PENDING' and internalOrderId is not null "
			+ "and (reconStatus <>'R4' or reconStatus is null) order by sno", nativeQuery = true)
	List<TransactionDetails> getAllRecordsForPayout12Hours();

	@Query(value = "select * from TransactionDetails tr "
			+ "where created between (NOW() - INTERVAL 2448 MINUTE) and (NOW() - INTERVAL 1440 MINUTE) "
			+ "and transactionStatus = 'PENDING' and internalOrderId is not null "
			+ "and (reconStatus <>'R5' or reconStatus is null) order by sno", nativeQuery = true)
	List<TransactionDetails> getAllRecordsForPayout24Hours();

	@Query(value = "select * from TransactionDetails tr "
			+ "where created between (NOW() - INTERVAL 3896 MINUTE) and (NOW() - INTERVAL 2448 MINUTE) "
			+ "and internalOrderId is not null "
			+ "and (reconStatus <>'R6' or reconStatus is null) order by sno", nativeQuery = true)
	List<TransactionDetails> getAllRecordsForPayout48Hours();

	@Query(value = "select * from TransactionDetails tr " + "where internalOrderId is not null "
			+ "and reconStatus is null order by sno", nativeQuery = true)
	List<TransactionDetails> getAllRecordsForPayoutOneTime();

	@Query(value = "select * from TransactionDetails tr where internalOrderId is not null and transactionStatus in ('PENDING', 'ACCEPTED') and created between (NOW() - INTERVAL 1200 MINUTE) and (NOW() - INTERVAL 2 MINUTE) order by sno desc", nativeQuery = true)
	List<TransactionDetails> getAllRecordsForPayoutHourly();

	@Query(value = "select * from TransactionDetails tr where internalOrderId is not null and transactionStatus in ('SUCCESS') and utrid is null and created between (NOW() - INTERVAL 1200 MINUTE) and (NOW() - INTERVAL 2 MINUTE) order by sno desc", nativeQuery = true)
	List<TransactionDetails> getAllRecordsPendingUTRPayoutHourly();

	@Query(value = "SELECT * FROM TransactionDetails  where transactionStatus like :transactionStatus and transactionType like :transactionType and date(created) between :dateFrom and :dateTo and merchantId = :merchantId order by sno desc", nativeQuery = true)
	List<TransactionDetails> getSearchByDetails(@Param("transactionStatus") String transactionStatus,
			@Param("transactionType") String transactionType, @Param("dateFrom") String dateFrom,
			@Param("dateTo") String dateTo, @Param("merchantId") String merchantId);

	@Query(value = "SELECT * FROM TransactionDetails  where transactionStatus like :transactionStatus and transactionType like :transactionType and orderId = :orderId and merchantId = :merchantId order by sno desc", nativeQuery = true)
	List<TransactionDetails> getSearchByMerchantIdAndStatusAndTransactionType(
			@Param("transactionStatus") String transactionStatus,
			@Param("transactionType") String transactionType, @Param("merchantId") String merchantId,
			@Param("orderId") String orderId);

	TransactionDetails findByInternalOrderId(String orderId);

	@Query(value = "SELECT * FROM TransactionDetails  where merchantid = :merchantId order by sno desc limit 5000", nativeQuery = true)
	List<TransactionDetails> getByMerchantId(@Param("merchantId") String merchantid);

	@Query(value = "SELECT * FROM TransactionDetails  where orderId = :orderId ", nativeQuery = true)
	List<TransactionDetails> getByOrderId(@Param("orderId") String orderId);

	@Query(value = "SELECT * FROM TransactionDetails  where transactionType like :transactionType order by sno desc limit 5000", nativeQuery = true)
	List<TransactionDetails> getSearchByTransactionType(@Param("transactionType") String transactionType);

	@Query(value = "SELECT * FROM TransactionDetails  where transactionType like :transactionType and merchantId = :merchantId", nativeQuery = true)
	List<TransactionDetails> getSearchByMerchantIdAndTransactionType(@Param("transactionType") String transactionType,
			@Param("merchantId") String merchantId);

	@Query(value = "SELECT * FROM TransactionDetails  where transactionStatus like :transactionStatus and transactionType like :transactionType and merchantId = :merchantId order by sno desc", nativeQuery = true)
	List<TransactionDetails> getByStatusAndMerchantAndType(@Param("transactionStatus") String transactionStatus,
			@Param("transactionType") String transactionType, @Param("merchantId") String merchantId);

	// dashbord report
	@Query(value = "SELECT merchantId, transactionType,  count(1) cnt, SUM(amount) totalAmt FROM TransactionDetails WHERE transactionStatus = :status and DATE(created) between :fromDate and :endDate GROUP BY transactionType , merchantId;", nativeQuery = true)
	List<IMerchantWisePgWiseSum> getByMerchantWisePgWiseSumQuery(@Param("fromDate") String fromDate,
			@Param("endDate") String endDate, @Param("status") String status);

	@Query(value = "SELECT  transactionType,  count(1) cnt, SUM(amount) totalAmt, pgname, transactionStatus FROM TransactionDetails WHERE transactionStatus = :status and DATE(created) between :fromDate and :endDate GROUP BY transactionType;", nativeQuery = true)
	List<IPgTypeAndCountByStatusAndDate> getPgTypeAndCountByStatusAndDate(@Param("fromDate") String fromDate,
			@Param("endDate") String endDate, @Param("status") String status);

	@Query(value = "select hour(created) as Hour, count(1) as Count from TransactionDetails where date(created) = :fromDate and transactionStatus = :status group by  hour(created);", nativeQuery = true)
	List<IHourandCountStatus> getHourandCountStatusAndDate(@Param("fromDate") String fromDate,
			@Param("status") String status);

	@Query(value = "select minute(created) as minutes, count(1) as Count from TransactionDetails where created between NOW() - INTERVAL 60 MINUTE AND NOW() - INTERVAL 5 MINUTE and transactionStatus = :status  group by  minute(created) order by minutes desc;", nativeQuery = true)
	List<IMinuteandCountStatus> getMinuteandCountByStatus(@Param("status") String status);

	// @Query(value = "select data.status, data.Count, data.amountSum, data.Count /
	// (sum(data.Count) over ()) * 100 statusPercent from (select status, count(1)
	// as Count, sum(amount)/100 amountSum from transaction_details where
	// date(created) between :startDate and :endDate group by status) as data;",
	// nativeQuery = true)
	// List<IStatusCountTrx> getStatusCount(@Param("startDate") String startDate,
	// @Param("endDate") String endDate);
	@Query(value = "select transactionStatus status, sum(amount) amountSum, count(1) count from TransactionDetails where date(created) between :startDate and :endDate group by transactionStatus", nativeQuery = true)
	List<IStatusCountTrx> getStatusCount(@Param("startDate") String startDate,
			@Param("endDate") String endDate);

	@Query(value = "SELECT merchantId,max(created) last_trxn, amount amt FROM TransactionDetails where date(created)=:startDate  group by merchantId", nativeQuery = true)
	List<ILastTrxMercList> getLastTrxMerchList(@Param("startDate") String startDate);

	@Query(value = "SELECT MINUTE(created) AS minutes,transactionStatus status, COUNT(1) AS cnt, sum(amount) total FROM TransactionDetails WHERE created BETWEEN NOW() - INTERVAL 60 MINUTE AND NOW() - INTERVAL 5 MINUTE  GROUP BY MINUTE(created), transactionStatus ORDER BY  transactionStatus,MINUTE(created) DESC", nativeQuery = true)
	List<StatusAndMinute> getStatusAndMinuteWiseCount();


	@Query(value = "select hour(created) as Hour,transactionStatus status, count(1) as Count from TransactionDetails where date(created) = :fromDate  group by transactionStatus ,hour(created) ", nativeQuery = true)
	List<IHourandStatusWise> getHourandStatusWiseCountAndDate(@Param("fromDate") String fromDate);
  

	List<TransactionDetails> findAllByMerchantId(String id);
	List<TransactionDetails> findAllByMerchantIdAndPgId(String id,String pgid);
	List<TransactionDetails> findAllByMerchantIdAndInternalOrderId(String id,String inOderId);
	List<TransactionDetails> findAllByMerchantIdAndInternalOrderIdAndPgId(String id,String inOderId,String pgid);
	List<TransactionDetails> findAllByInternalOrderIdAndPgId(String inOderId,String pgid);
	List<TransactionDetails> findAllByInternalOrderId(String inOderId);

	@Query(value = "select * "
	+ " from TransactionDetails tr where tr.pgId = :pg_id", nativeQuery = true)
List<TransactionDetails> getTransactionByPgIdWithOutDate(@Param("pg_id") String pg_id);

@Query(value = "select * "
			+ " from TransactionDetails tr where tr.pgId = :pg_id and date(created) between :start_date and :end_date", nativeQuery = true)
	List<TransactionDetails> getTransactionByPgId(@Param("pg_id") String pg_id, @Param("start_date") String start_date,
			@Param("end_date") String end_date);

			@Query(value = "select * "
			+ " from TransactionDetails tr where tr.merchantId= :merchant_id and tr.internalOrderId= :merchant_order_id and tr.pgId = :pg_id  and date(created) between :start_date and :end_date ", nativeQuery = true)
	List<TransactionDetails> findAllByMerIdAndInterrnalOrAndPgId(@Param("merchant_id") String merchant_id,
			@Param("merchant_order_id") String merchant_order_id, @Param("pg_id") String pg_id,
			@Param("start_date") String start_date, @Param("end_date") String end_date);

			List<TransactionDetails> findByinternalOrderIdIn (List<String> inOderids);
			List<TransactionDetails> findByorderIdIn(List<String> oderIdsListInString2);
			List<TransactionDetails> findByutridIn(List<String> oderIdsListInString2);


			@Query(value = "select *"
			+ " from TransactionDetails tr where date(created) between :start_date and :end_date limit 10000", nativeQuery = true)
	List<TransactionDetails> findAllByTrDate(@Param("start_date") String start_date,
			@Param("end_date") String end_date);

			@Query(value = "select *"
			+ " from TransactionDetails tr where  date(created) >= :start_date  ", nativeQuery = true)
	List<TransactionDetails> getDataStartDate(@Param("start_date") String start_date);

	@Query(value = "*"
	+ " from TransactionDetails tr where  date(created) <= :end_date ", nativeQuery = true)
List<TransactionDetails> getDataEndDate(@Param("end_date") String end_date);
@Query(value = "select * from TransactionDetails where transactionMessage like '% :trx_msg %' and date(created) between :start_date and :end_date", nativeQuery = true)
List<TransactionDetails> findAllByTrx_msg_Like(@Param("trx_msg") String trx_msg, @Param("start_date") String start_date,
		@Param("end_date") String end_date);
	// @Query(value = "select tr.id, tr.created, tr.updated, tr.amount,tr.email_id, tr.merchant_alerturl, tr.order_note, tr.recon_status, tr.source, tr.card_number, tr.cust_order_id, tr.merchant_id, tr.merchant_order_id, tr.merchant_returnurl, tr.orderid, tr.payment_code, tr.payment_mode, tr.payment_option, tr.pg_id, tr.pg_orderid, tr.pg_type, tr.status, tr.txt_msg, tr.txtpgtime, tr.userid, tr.vpaupi"
	// 		+ " from TransactionDetails tr where upper(pg_type)= upper(:pgType) and upper(payment_option)= upper(:payment_option) and tr.merchant_id= :merchant_id  and tr.pg_id = :pg_id and tr.id = :trId  ", nativeQuery = true)
	// List<TransactionDetails> findAllByPgTypeAndPayOptionAndMerIdAndTrIdAndPgId(
	// 		@Param("merchant_id") String merchant_id, @Param("payment_option") String payment_option,
	// 		@Param("pgType") String pgType,
	// 		@Param("pg_id") String pg_id,
	// 		@Param("trId") String trId);


	
	@Query(value = "SELECT * FROM TransactionDetails where callBackFlag = 'TRUE' and callBackURL is not null", nativeQuery = true)
	List<TransactionDetails> getByCallBackStatus();

    TransactionDetails findByinternalOrderId(String o);
}
