package io.asktech.payout.dto.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.asktech.payout.enums.SuccessCode;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SuccessResponseDto {
	private List<String> msg = new ArrayList<>();
	private boolean status = true;
	private SuccessCode successCode;
	private int statusCode = 200;
    private Map<String, Object> extraData = new HashMap<>();
}
