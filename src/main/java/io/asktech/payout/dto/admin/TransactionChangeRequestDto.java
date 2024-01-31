package io.asktech.payout.dto.admin;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionChangeRequestDto {
	private String uuid;
	private List<UpdateTransactionDetailsRequestDto> updateDataDto = new ArrayList<>();
}
