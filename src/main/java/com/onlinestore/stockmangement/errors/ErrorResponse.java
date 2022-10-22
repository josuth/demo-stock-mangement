package com.onlinestore.stockmangement.errors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
	int code;
	String errorMessage;
}
