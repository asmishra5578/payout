package io.asktech.payout.service.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.asktech.payout.dto.admin.TransactionChangeRequestDto;
import io.asktech.payout.dto.admin.TransactionChangeResponce;
import io.asktech.payout.dto.admin.TransactionChangeResponceList;
import io.asktech.payout.dto.admin.TransactionChangeResponceListDto;
import io.asktech.payout.dto.admin.UpdateTransactionDetailsRequestDto;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.admin.TransactionChangeRequest;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.repository.admin.TransactionChangeRequestRepo;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;

@Service
public class TransactionChangeRequestService {
	@Autowired
	private TransactionChangeRequestRepo transactionChangeRequestRepo;
	@Autowired
	private TransactionDetailsRepo transactionDetailsRepo;

	int scount = 0;
	int fcount = 0;

	public TransactionChangeResponceListDto updateTransactionStatus(
			TransactionChangeRequestDto transactionChangeRequestDto) throws ValidationExceptions {
		TransactionChangeResponceListDto resDto = new TransactionChangeResponceListDto();
		List<UpdateTransactionDetailsRequestDto> updateSuccessDataDto = new ArrayList<>();
		List<UpdateTransactionDetailsRequestDto> updateFailedDataDto = new ArrayList<>();
		List<UpdateTransactionDetailsRequestDto> updatePendingDataDto = new ArrayList<>();
		List<UpdateTransactionDetailsRequestDto> updateRefundDataDto = new ArrayList<>();
		List<String> successOrderIds = new ArrayList<>();
		List<String> failedOrderIds = new ArrayList<>();
		List<String> pendingOrderIds = new ArrayList<>();
		List<String> refundOrderIds = new ArrayList<>();
		transactionChangeRequestDto.getUpdateDataDto().forEach(o -> {
			if (o.getTransactionStatus().equals("SUCCESS")) {
				updateSuccessDataDto.add(o);
				successOrderIds.add(o.getInternalOrderId());
			}
			if (o.getTransactionStatus().equals("FAILURE")) {
				updateFailedDataDto.add(o);
				failedOrderIds.add(o.getInternalOrderId());
			}
			if (o.getTransactionStatus().equals("PENDING")) {
				updatePendingDataDto.add(o);
				pendingOrderIds.add(o.getInternalOrderId());
			}
			if (o.getTransactionStatus().equals("REFUND")) {
				updateRefundDataDto.add(o);
				refundOrderIds.add(o.getInternalOrderId());
			}
		});
		if (!updateSuccessDataDto.isEmpty()) {
			TransactionChangeRequest transactionChangeRequest = new TransactionChangeRequest();
			transactionChangeRequest.setComment(updateSuccessDataDto.get(0).getComment());
			transactionChangeRequest.setOrderIds(String.join(",", successOrderIds));
			transactionChangeRequest.setStatus(updateSuccessDataDto.get(0).getTransactionStatus());
			transactionChangeRequest.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			List<TransactionChangeResponceList> transactionChangeResponceList = new ArrayList<>();
			scount = 0;
			fcount = 0;
			updateSuccessDataDto.forEach(o -> {
				if (o.getInternalOrderId().trim().length() > 5) {
					TransactionDetails transactionDetails = transactionDetailsRepo
							.findByInternalOrderId(o.getInternalOrderId());
					TransactionChangeResponceList transactionChangeResponceList2 = new TransactionChangeResponceList();
					if (transactionDetails == null) {
						transactionChangeResponceList2
								.setComment("Status update is fail because Internal OrderId is not found in database");
						transactionChangeResponceList2.setOrderIds(o.getInternalOrderId());
						transactionChangeResponceList2.setStatus("FAILURE");
						transactionChangeResponceList.add(transactionChangeResponceList2);
						fcount++;
					} else {
						if (!StringUtils.isEmpty(o.getUtrid())) {
							transactionDetails.setUtrid(o.getUtrid());
						}
						if (!StringUtils.isEmpty(o.getReferenceId())) {
							transactionDetails.setReferenceId(o.getReferenceId());
						}
						if (!StringUtils.isEmpty(o.getTransactionStatus())) {
							transactionDetails.setTransactionStatus(o.getTransactionStatus());
						}
						if (!StringUtils.isEmpty(o.getTransactionMessage())) {
							transactionDetails.setTransactionMessage(o.getTransactionMessage());
						}
						if (!StringUtils.isEmpty(o.getComment())) {
							transactionDetails.setErrorMessage(o.getComment());
						}
						if (!StringUtils.isEmpty(o.getCallBackFlag())) {
							transactionDetails.setCallBackFlag(o.getCallBackFlag());
						}
						transactionDetails = transactionDetailsRepo.save(transactionDetails);
						transactionChangeResponceList2.setComment(transactionDetails.getErrorMessage());
						transactionChangeResponceList2.setOrderIds(transactionDetails.getInternalOrderId());
						transactionChangeResponceList2.setStatus(transactionDetails.getTransactionStatus());
						transactionChangeResponceList.add(transactionChangeResponceList2);
						scount++;
					}
				}
			});
			transactionChangeRequest.setSucessCount(scount);
			transactionChangeRequest.setFailCount(fcount);
			transactionChangeRequest.setCount(successOrderIds.size());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			TransactionChangeResponce transactionChangeResponce = new TransactionChangeResponce();
			transactionChangeResponce.setComment(transactionChangeRequest.getComment());
			transactionChangeResponce.setTotalCount(successOrderIds.size());
			transactionChangeResponce.setOrderIds(transactionChangeRequest.getOrderIds());
			transactionChangeResponce.setStatus(transactionChangeRequest.getStatus());
			transactionChangeResponce.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeResponce.setFailCount(fcount);
			transactionChangeResponce.setSucessCount(scount);
			transactionChangeResponce.setTransactionChangeResponceList(transactionChangeResponceList);
			resDto.setSuccessDataTransactionChangeResponce(transactionChangeResponce);
		}
		if (!updateFailedDataDto.isEmpty()) {
			TransactionChangeRequest transactionChangeRequest = new TransactionChangeRequest();
			transactionChangeRequest.setComment(updateFailedDataDto.get(0).getComment());
			transactionChangeRequest.setOrderIds(String.join(",", failedOrderIds));
			transactionChangeRequest.setStatus(updateFailedDataDto.get(0).getTransactionStatus());
			transactionChangeRequest.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			List<TransactionChangeResponceList> transactionChangeResponceList = new ArrayList<>();
			scount = 0;
			fcount = 0;
			updateFailedDataDto.forEach(o -> {
				if (o.getInternalOrderId().trim().length() > 5) {
					TransactionDetails transactionDetails = transactionDetailsRepo
							.findByInternalOrderId(o.getInternalOrderId());
					TransactionChangeResponceList transactionChangeResponceList2 = new TransactionChangeResponceList();
					if (transactionDetails == null) {
						transactionChangeResponceList2
								.setComment("Status update is fail because Internal OrderId is not found in database");
						transactionChangeResponceList2.setOrderIds(o.getInternalOrderId());
						transactionChangeResponceList2.setStatus("FAILURE");
						transactionChangeResponceList.add(transactionChangeResponceList2);
						fcount++;
					} else {
						if (!StringUtils.isEmpty(o.getUtrid())) {
							transactionDetails.setUtrid(o.getUtrid());
						}
						if (!StringUtils.isEmpty(o.getReferenceId())) {
							transactionDetails.setReferenceId(o.getReferenceId());
						}
						if (!StringUtils.isEmpty(o.getTransactionStatus())) {
							transactionDetails.setTransactionStatus(o.getTransactionStatus());
						}
						if (!StringUtils.isEmpty(o.getTransactionMessage())) {
							transactionDetails.setTransactionMessage(o.getTransactionMessage());
						}
						if (!StringUtils.isEmpty(o.getComment())) {
							transactionDetails.setErrorMessage(o.getComment());
						}
						if (!StringUtils.isEmpty(o.getCallBackFlag())) {
							transactionDetails.setCallBackFlag(o.getCallBackFlag());
						}
						transactionDetails = transactionDetailsRepo.save(transactionDetails);
						transactionChangeResponceList2.setComment(transactionDetails.getErrorMessage());
						transactionChangeResponceList2.setOrderIds(transactionDetails.getInternalOrderId());
						transactionChangeResponceList2.setStatus(transactionDetails.getTransactionStatus());
						transactionChangeResponceList.add(transactionChangeResponceList2);
						scount++;
					}
				}
			});
			transactionChangeRequest.setSucessCount(scount);
			transactionChangeRequest.setFailCount(fcount);
			transactionChangeRequest.setCount(failedOrderIds.size());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			TransactionChangeResponce transactionChangeResponce = new TransactionChangeResponce();
			transactionChangeResponce.setComment(transactionChangeRequest.getComment());
			transactionChangeResponce.setTotalCount(failedOrderIds.size());
			transactionChangeResponce.setOrderIds(transactionChangeRequest.getOrderIds());
			transactionChangeResponce.setStatus(transactionChangeRequest.getStatus());
			transactionChangeResponce.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeResponce.setFailCount(fcount);
			transactionChangeResponce.setSucessCount(scount);
			transactionChangeResponce.setTransactionChangeResponceList(transactionChangeResponceList);
			resDto.setFailedDataTransactionChangeResponce(transactionChangeResponce);
		}
		if (!updatePendingDataDto.isEmpty()) {
			TransactionChangeRequest transactionChangeRequest = new TransactionChangeRequest();
			transactionChangeRequest.setComment(updatePendingDataDto.get(0).getComment());
			transactionChangeRequest.setOrderIds(String.join(",", pendingOrderIds));
			transactionChangeRequest.setStatus(updatePendingDataDto.get(0).getTransactionStatus());
			transactionChangeRequest.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			List<TransactionChangeResponceList> transactionChangeResponceList = new ArrayList<>();
			scount = 0;
			fcount = 0;
			updatePendingDataDto.forEach(o -> {
				if (o.getInternalOrderId().trim().length() > 5) {
					TransactionDetails transactionDetails = transactionDetailsRepo
							.findByInternalOrderId(o.getInternalOrderId());
					TransactionChangeResponceList transactionChangeResponceList2 = new TransactionChangeResponceList();
					if (transactionDetails == null) {
						transactionChangeResponceList2
								.setComment("Status update is fail because Internal OrderId is not found in database");
						transactionChangeResponceList2.setOrderIds(o.getInternalOrderId());
						transactionChangeResponceList2.setStatus("FAILURE");
						transactionChangeResponceList.add(transactionChangeResponceList2);
						fcount++;
					} else {
						if (!StringUtils.isEmpty(o.getUtrid())) {
							transactionDetails.setUtrid(o.getUtrid());
						}
						if (!StringUtils.isEmpty(o.getReferenceId())) {
							transactionDetails.setReferenceId(o.getReferenceId());
						}
						if (!StringUtils.isEmpty(o.getTransactionStatus())) {
							transactionDetails.setTransactionStatus(o.getTransactionStatus());
						}
						if (!StringUtils.isEmpty(o.getTransactionMessage())) {
							transactionDetails.setTransactionMessage(o.getTransactionMessage());
						}
						if (!StringUtils.isEmpty(o.getComment())) {
							transactionDetails.setErrorMessage(o.getComment());
						}
						if (!StringUtils.isEmpty(o.getCallBackFlag())) {
							transactionDetails.setCallBackFlag(o.getCallBackFlag());
						}
						transactionDetails = transactionDetailsRepo.save(transactionDetails);
						transactionChangeResponceList2.setComment(transactionDetails.getErrorMessage());
						transactionChangeResponceList2.setOrderIds(transactionDetails.getInternalOrderId());
						transactionChangeResponceList2.setStatus(transactionDetails.getTransactionStatus());
						transactionChangeResponceList.add(transactionChangeResponceList2);
						scount++;
					}
				}
			});
			transactionChangeRequest.setSucessCount(scount);
			transactionChangeRequest.setFailCount(fcount);
			transactionChangeRequest.setCount(pendingOrderIds.size());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			TransactionChangeResponce transactionChangeResponce = new TransactionChangeResponce();
			transactionChangeResponce.setComment(transactionChangeRequest.getComment());
			transactionChangeResponce.setTotalCount(pendingOrderIds.size());
			transactionChangeResponce.setOrderIds(transactionChangeRequest.getOrderIds());
			transactionChangeResponce.setStatus(transactionChangeRequest.getStatus());
			transactionChangeResponce.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeResponce.setFailCount(fcount);
			transactionChangeResponce.setSucessCount(scount);
			transactionChangeResponce.setTransactionChangeResponceList(transactionChangeResponceList);
			resDto.setPendingDataTransactionChangeResponce(transactionChangeResponce);
		}
		if (!updateRefundDataDto.isEmpty()) {
			TransactionChangeRequest transactionChangeRequest = new TransactionChangeRequest();
			transactionChangeRequest.setComment(updateRefundDataDto.get(0).getComment());
			transactionChangeRequest.setOrderIds(String.join(",", refundOrderIds));
			transactionChangeRequest.setStatus(updateRefundDataDto.get(0).getTransactionStatus());
			transactionChangeRequest.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			List<TransactionChangeResponceList> transactionChangeResponceList = new ArrayList<>();
			scount = 0;
			fcount = 0;
			updateRefundDataDto.forEach(o -> {
				if (o.getInternalOrderId().trim().length() > 5) {
					TransactionDetails transactionDetails = transactionDetailsRepo
							.findByInternalOrderId(o.getInternalOrderId());
					TransactionChangeResponceList transactionChangeResponceList2 = new TransactionChangeResponceList();
					if (transactionDetails == null) {
						transactionChangeResponceList2
								.setComment("Status update is fail because Internal OrderId is not found in database");
						transactionChangeResponceList2.setOrderIds(o.getInternalOrderId());
						transactionChangeResponceList2.setStatus("FAILURE");
						transactionChangeResponceList.add(transactionChangeResponceList2);
						fcount++;
					} else {
						if (!StringUtils.isEmpty(o.getUtrid())) {
							transactionDetails.setUtrid(o.getUtrid());
						}
						if (!StringUtils.isEmpty(o.getReferenceId())) {
							transactionDetails.setReferenceId(o.getReferenceId());
						}
						if (!StringUtils.isEmpty(o.getTransactionStatus())) {
							transactionDetails.setTransactionStatus(o.getTransactionStatus());
						}
						if (!StringUtils.isEmpty(o.getTransactionMessage())) {
							transactionDetails.setTransactionMessage(o.getTransactionMessage());
						}
						if (!StringUtils.isEmpty(o.getComment())) {
							transactionDetails.setErrorMessage(o.getComment());
						}
						if (!StringUtils.isEmpty(o.getCallBackFlag())) {
							transactionDetails.setCallBackFlag(o.getCallBackFlag());
						}
						transactionDetails = transactionDetailsRepo.save(transactionDetails);
						transactionChangeResponceList2.setComment(transactionDetails.getErrorMessage());
						transactionChangeResponceList2.setOrderIds(transactionDetails.getInternalOrderId());
						transactionChangeResponceList2.setStatus(transactionDetails.getTransactionStatus());
						transactionChangeResponceList.add(transactionChangeResponceList2);
						scount++;
					}
				}
			});
			transactionChangeRequest.setSucessCount(scount);
			transactionChangeRequest.setFailCount(fcount);
			transactionChangeRequest.setCount(refundOrderIds.size());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			TransactionChangeResponce transactionChangeResponce = new TransactionChangeResponce();
			transactionChangeResponce.setComment(transactionChangeRequest.getComment());
			transactionChangeResponce.setTotalCount(refundOrderIds.size());
			transactionChangeResponce.setOrderIds(transactionChangeRequest.getOrderIds());
			transactionChangeResponce.setStatus(transactionChangeRequest.getStatus());
			transactionChangeResponce.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeResponce.setFailCount(fcount);
			transactionChangeResponce.setSucessCount(scount);
			transactionChangeResponce.setTransactionChangeResponceList(transactionChangeResponceList);
			resDto.setRefundDataTransactionChangeResponce(transactionChangeResponce);
		}
		return resDto;
	}

	public List<TransactionChangeRequest> getallTransactionChangeRequest() {
		return transactionChangeRequestRepo.findAll();
	}

	@Lazy
	@Async
	public void updateBulkFileTransactionStatus(TransactionChangeRequestDto transactionChangeRequestDto) {
		TransactionChangeResponceListDto resDto = new TransactionChangeResponceListDto();
		List<UpdateTransactionDetailsRequestDto> updateSuccessDataDto = new ArrayList<>();
		List<UpdateTransactionDetailsRequestDto> updateFailedDataDto = new ArrayList<>();
		List<UpdateTransactionDetailsRequestDto> updatePendingDataDto = new ArrayList<>();
		List<UpdateTransactionDetailsRequestDto> updateRefundDataDto = new ArrayList<>();
		List<String> successOrderIds = new ArrayList<>();
		List<String> failedOrderIds = new ArrayList<>();
		List<String> pendingOrderIds = new ArrayList<>();
		List<String> refundOrderIds = new ArrayList<>();
		transactionChangeRequestDto.getUpdateDataDto().forEach(o -> {
			if (o.getTransactionStatus().equals("SUCCESS")) {
				updateSuccessDataDto.add(o);
				successOrderIds.add(o.getInternalOrderId());
			}
			if (o.getTransactionStatus().equals("FAILURE")) {
				updateFailedDataDto.add(o);
				failedOrderIds.add(o.getInternalOrderId());
			}
			if (o.getTransactionStatus().equals("PENDING")) {
				updatePendingDataDto.add(o);
				pendingOrderIds.add(o.getInternalOrderId());
			}
			if (o.getTransactionStatus().equals("REFUND")) {
				updateRefundDataDto.add(o);
				refundOrderIds.add(o.getInternalOrderId());
			}
		});
		if (!updateSuccessDataDto.isEmpty()) {
			TransactionChangeRequest transactionChangeRequest = new TransactionChangeRequest();
			transactionChangeRequest.setComment(updateSuccessDataDto.get(0).getComment());
			transactionChangeRequest.setOrderIds(String.join(",", successOrderIds));
			transactionChangeRequest.setStatus(updateSuccessDataDto.get(0).getTransactionStatus());
			transactionChangeRequest.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			List<TransactionChangeResponceList> transactionChangeResponceList = new ArrayList<>();
			scount = 0;
			fcount = 0;
			updateSuccessDataDto.forEach(o -> {
				if (o.getInternalOrderId().trim().length() > 5) {
					TransactionDetails transactionDetails = transactionDetailsRepo
							.findByInternalOrderId(o.getInternalOrderId());
					TransactionChangeResponceList transactionChangeResponceList2 = new TransactionChangeResponceList();
					if (transactionDetails == null) {
						transactionChangeResponceList2
								.setComment("Status update is fail because Internal OrderId is not found in database");
						transactionChangeResponceList2.setOrderIds(o.getInternalOrderId());
						transactionChangeResponceList2.setStatus("FAILURE");
						transactionChangeResponceList.add(transactionChangeResponceList2);
						fcount++;
					} else {
						if (!StringUtils.isEmpty(o.getUtrid())) {
							transactionDetails.setUtrid(o.getUtrid());
						}
						if (!StringUtils.isEmpty(o.getReferenceId())) {
							transactionDetails.setReferenceId(o.getReferenceId());
						}
						if (!StringUtils.isEmpty(o.getTransactionStatus())) {
							transactionDetails.setTransactionStatus(o.getTransactionStatus());
						}
						if (!StringUtils.isEmpty(o.getTransactionMessage())) {
							transactionDetails.setTransactionMessage(o.getTransactionMessage());
						}
						if (!StringUtils.isEmpty(o.getComment())) {
							transactionDetails.setErrorMessage(o.getComment());
						}
						if (!StringUtils.isEmpty(o.getCallBackFlag())) {
							transactionDetails.setCallBackFlag(o.getCallBackFlag());
						}
						transactionDetails = transactionDetailsRepo.save(transactionDetails);
						transactionChangeResponceList2.setComment(transactionDetails.getErrorMessage());
						transactionChangeResponceList2.setOrderIds(transactionDetails.getInternalOrderId());
						transactionChangeResponceList2.setStatus(transactionDetails.getTransactionStatus());
						transactionChangeResponceList.add(transactionChangeResponceList2);
						scount++;
					}
				}
			});
			transactionChangeRequest.setSucessCount(scount);
			transactionChangeRequest.setFailCount(fcount);
			transactionChangeRequest.setCount(successOrderIds.size());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			TransactionChangeResponce transactionChangeResponce = new TransactionChangeResponce();
			transactionChangeResponce.setComment(transactionChangeRequest.getComment());
			transactionChangeResponce.setTotalCount(successOrderIds.size());
			transactionChangeResponce.setOrderIds(transactionChangeRequest.getOrderIds());
			transactionChangeResponce.setStatus(transactionChangeRequest.getStatus());
			transactionChangeResponce.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeResponce.setFailCount(fcount);
			transactionChangeResponce.setSucessCount(scount);
			transactionChangeResponce.setTransactionChangeResponceList(transactionChangeResponceList);
			resDto.setSuccessDataTransactionChangeResponce(transactionChangeResponce);
		}
		if (!updateFailedDataDto.isEmpty()) {
			TransactionChangeRequest transactionChangeRequest = new TransactionChangeRequest();
			transactionChangeRequest.setComment(updateFailedDataDto.get(0).getComment());
			transactionChangeRequest.setOrderIds(String.join(",", failedOrderIds));
			transactionChangeRequest.setStatus(updateFailedDataDto.get(0).getTransactionStatus());
			transactionChangeRequest.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			List<TransactionChangeResponceList> transactionChangeResponceList = new ArrayList<>();
			scount = 0;
			fcount = 0;
			updateFailedDataDto.forEach(o -> {
				if (o.getInternalOrderId().trim().length() > 5) {
					TransactionDetails transactionDetails = transactionDetailsRepo
							.findByInternalOrderId(o.getInternalOrderId());
					TransactionChangeResponceList transactionChangeResponceList2 = new TransactionChangeResponceList();
					if (transactionDetails == null) {
						transactionChangeResponceList2
								.setComment("Status update is fail because Internal OrderId is not found in database");
						transactionChangeResponceList2.setOrderIds(o.getInternalOrderId());
						transactionChangeResponceList2.setStatus("FAILURE");
						transactionChangeResponceList.add(transactionChangeResponceList2);
						fcount++;
					} else {
						if (!StringUtils.isEmpty(o.getUtrid())) {
							transactionDetails.setUtrid(o.getUtrid());
						}
						if (!StringUtils.isEmpty(o.getReferenceId())) {
							transactionDetails.setReferenceId(o.getReferenceId());
						}
						if (!StringUtils.isEmpty(o.getTransactionStatus())) {
							transactionDetails.setTransactionStatus(o.getTransactionStatus());
						}
						if (!StringUtils.isEmpty(o.getTransactionMessage())) {
							transactionDetails.setTransactionMessage(o.getTransactionMessage());
						}
						if (!StringUtils.isEmpty(o.getComment())) {
							transactionDetails.setErrorMessage(o.getComment());
						}
						if (!StringUtils.isEmpty(o.getCallBackFlag())) {
							transactionDetails.setCallBackFlag(o.getCallBackFlag());
						}
						transactionDetails = transactionDetailsRepo.save(transactionDetails);
						transactionChangeResponceList2.setComment(transactionDetails.getErrorMessage());
						transactionChangeResponceList2.setOrderIds(transactionDetails.getInternalOrderId());
						transactionChangeResponceList2.setStatus(transactionDetails.getTransactionStatus());
						transactionChangeResponceList.add(transactionChangeResponceList2);
						scount++;
					}
				}
			});
			transactionChangeRequest.setSucessCount(scount);
			transactionChangeRequest.setFailCount(fcount);
			transactionChangeRequest.setCount(failedOrderIds.size());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			TransactionChangeResponce transactionChangeResponce = new TransactionChangeResponce();
			transactionChangeResponce.setComment(transactionChangeRequest.getComment());
			transactionChangeResponce.setTotalCount(failedOrderIds.size());
			transactionChangeResponce.setOrderIds(transactionChangeRequest.getOrderIds());
			transactionChangeResponce.setStatus(transactionChangeRequest.getStatus());
			transactionChangeResponce.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeResponce.setFailCount(fcount);
			transactionChangeResponce.setSucessCount(scount);
			transactionChangeResponce.setTransactionChangeResponceList(transactionChangeResponceList);
			resDto.setFailedDataTransactionChangeResponce(transactionChangeResponce);
		}
		if (!updatePendingDataDto.isEmpty()) {
			TransactionChangeRequest transactionChangeRequest = new TransactionChangeRequest();
			transactionChangeRequest.setComment(updatePendingDataDto.get(0).getComment());
			transactionChangeRequest.setOrderIds(String.join(",", pendingOrderIds));
			transactionChangeRequest.setStatus(updatePendingDataDto.get(0).getTransactionStatus());
			transactionChangeRequest.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			List<TransactionChangeResponceList> transactionChangeResponceList = new ArrayList<>();
			scount = 0;
			fcount = 0;
			updatePendingDataDto.forEach(o -> {
				if (o.getInternalOrderId().trim().length() > 5) {
					TransactionDetails transactionDetails = transactionDetailsRepo
							.findByInternalOrderId(o.getInternalOrderId());
					TransactionChangeResponceList transactionChangeResponceList2 = new TransactionChangeResponceList();
					if (transactionDetails == null) {
						transactionChangeResponceList2
								.setComment("Status update is fail because Internal OrderId is not found in database");
						transactionChangeResponceList2.setOrderIds(o.getInternalOrderId());
						transactionChangeResponceList2.setStatus("FAILURE");
						transactionChangeResponceList.add(transactionChangeResponceList2);
						fcount++;
					} else {
						if (!StringUtils.isEmpty(o.getUtrid())) {
							transactionDetails.setUtrid(o.getUtrid());
						}
						if (!StringUtils.isEmpty(o.getReferenceId())) {
							transactionDetails.setReferenceId(o.getReferenceId());
						}
						if (!StringUtils.isEmpty(o.getTransactionStatus())) {
							transactionDetails.setTransactionStatus(o.getTransactionStatus());
						}
						if (!StringUtils.isEmpty(o.getTransactionMessage())) {
							transactionDetails.setTransactionMessage(o.getTransactionMessage());
						}
						if (!StringUtils.isEmpty(o.getComment())) {
							transactionDetails.setErrorMessage(o.getComment());
						}
						if (!StringUtils.isEmpty(o.getCallBackFlag())) {
							transactionDetails.setCallBackFlag(o.getCallBackFlag());
						}
						transactionDetails = transactionDetailsRepo.save(transactionDetails);
						transactionChangeResponceList2.setComment(transactionDetails.getErrorMessage());
						transactionChangeResponceList2.setOrderIds(transactionDetails.getInternalOrderId());
						transactionChangeResponceList2.setStatus(transactionDetails.getTransactionStatus());
						transactionChangeResponceList.add(transactionChangeResponceList2);
						scount++;
					}
				}
			});
			transactionChangeRequest.setSucessCount(scount);
			transactionChangeRequest.setFailCount(fcount);
			transactionChangeRequest.setCount(pendingOrderIds.size());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			TransactionChangeResponce transactionChangeResponce = new TransactionChangeResponce();
			transactionChangeResponce.setComment(transactionChangeRequest.getComment());
			transactionChangeResponce.setTotalCount(pendingOrderIds.size());
			transactionChangeResponce.setOrderIds(transactionChangeRequest.getOrderIds());
			transactionChangeResponce.setStatus(transactionChangeRequest.getStatus());
			transactionChangeResponce.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeResponce.setFailCount(fcount);
			transactionChangeResponce.setSucessCount(scount);
			transactionChangeResponce.setTransactionChangeResponceList(transactionChangeResponceList);
			resDto.setPendingDataTransactionChangeResponce(transactionChangeResponce);
		}
		if (!updateRefundDataDto.isEmpty()) {
			TransactionChangeRequest transactionChangeRequest = new TransactionChangeRequest();
			transactionChangeRequest.setComment(updateRefundDataDto.get(0).getComment());
			transactionChangeRequest.setOrderIds(String.join(",", refundOrderIds));
			transactionChangeRequest.setStatus(updateRefundDataDto.get(0).getTransactionStatus());
			transactionChangeRequest.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			List<TransactionChangeResponceList> transactionChangeResponceList = new ArrayList<>();
			scount = 0;
			fcount = 0;
			updateRefundDataDto.forEach(o -> {
				if (o.getInternalOrderId().trim().length() > 5) {
					TransactionDetails transactionDetails = transactionDetailsRepo
							.findByInternalOrderId(o.getInternalOrderId());
					TransactionChangeResponceList transactionChangeResponceList2 = new TransactionChangeResponceList();
					if (transactionDetails == null) {
						transactionChangeResponceList2
								.setComment("Status update is fail because Internal OrderId is not found in database");
						transactionChangeResponceList2.setOrderIds(o.getInternalOrderId());
						transactionChangeResponceList2.setStatus("FAILURE");
						transactionChangeResponceList.add(transactionChangeResponceList2);
						fcount++;
					} else {
						if (!StringUtils.isEmpty(o.getUtrid())) {
							transactionDetails.setUtrid(o.getUtrid());
						}
						if (!StringUtils.isEmpty(o.getReferenceId())) {
							transactionDetails.setReferenceId(o.getReferenceId());
						}
						if (!StringUtils.isEmpty(o.getTransactionStatus())) {
							transactionDetails.setTransactionStatus(o.getTransactionStatus());
						}
						if (!StringUtils.isEmpty(o.getTransactionMessage())) {
							transactionDetails.setTransactionMessage(o.getTransactionMessage());
						}
						if (!StringUtils.isEmpty(o.getComment())) {
							transactionDetails.setErrorMessage(o.getComment());
						}
						if (!StringUtils.isEmpty(o.getCallBackFlag())) {
							transactionDetails.setCallBackFlag(o.getCallBackFlag());
						}
						transactionDetails = transactionDetailsRepo.save(transactionDetails);
						transactionChangeResponceList2.setComment(transactionDetails.getErrorMessage());
						transactionChangeResponceList2.setOrderIds(transactionDetails.getInternalOrderId());
						transactionChangeResponceList2.setStatus(transactionDetails.getTransactionStatus());
						transactionChangeResponceList.add(transactionChangeResponceList2);
						scount++;
					}
				}
			});
			transactionChangeRequest.setSucessCount(scount);
			transactionChangeRequest.setFailCount(fcount);
			transactionChangeRequest.setCount(refundOrderIds.size());
			transactionChangeRequest = transactionChangeRequestRepo.save(transactionChangeRequest);
			TransactionChangeResponce transactionChangeResponce = new TransactionChangeResponce();
			transactionChangeResponce.setComment(transactionChangeRequest.getComment());
			transactionChangeResponce.setTotalCount(refundOrderIds.size());
			transactionChangeResponce.setOrderIds(transactionChangeRequest.getOrderIds());
			transactionChangeResponce.setStatus(transactionChangeRequest.getStatus());
			transactionChangeResponce.setUuid(transactionChangeRequestDto.getUuid());
			transactionChangeResponce.setFailCount(fcount);
			transactionChangeResponce.setSucessCount(scount);
			transactionChangeResponce.setTransactionChangeResponceList(transactionChangeResponceList);
			resDto.setRefundDataTransactionChangeResponce(transactionChangeResponce);
		}

	}
}
