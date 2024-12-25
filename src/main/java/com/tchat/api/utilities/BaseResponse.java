package com.tchat.api.utilities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse {
	private int statusCode;
	private String message;
	private Object data;
	private boolean isError;
}
