package com.tchat.api.utilities;

import org.springframework.http.ResponseEntity;

public class BaseAppController {
	protected ResponseEntity<BaseResponse> toResponse(Object data) {
		BaseResponse baseResponse = BaseResponse.builder()
				.data(data)
				.statusCode(200)
				.isError(false)
				.build();

		return ResponseEntity.ok(baseResponse);
	}

	protected ResponseEntity<BaseResponse> toResponse(Object data, int statusCode) {
		BaseResponse baseResponse = BaseResponse.builder()
				.data(data)
				.statusCode(statusCode)
				.isError(false)
				.build();

		return ResponseEntity.ok(baseResponse);
	}

	protected ResponseEntity<BaseResponse> toResponse(Object data, String message) {
		BaseResponse baseResponse = BaseResponse.builder()
				.data(data)
				.statusCode(200)
				.isError(false)
				.message(message)
				.build();

		return ResponseEntity.ok(baseResponse);
	}

	protected ResponseEntity<BaseResponse> toResponse(Object data, int statusCode, String message) {
		BaseResponse baseResponse = BaseResponse.builder()
				.data(data)
				.statusCode(statusCode)
				.isError(false)
				.message(message)
				.build();

		return ResponseEntity.ok(baseResponse);
	}

	protected ResponseEntity<BaseResponse> toResponse(Exception exception) {
		BaseResponse baseResponse = BaseResponse.builder()
				.statusCode(500)
				.isError(true)
				.message(exception.getLocalizedMessage())
				.build();

		return ResponseEntity.internalServerError().body(baseResponse);
	}

	protected ResponseEntity<BaseResponse> toResponse(Exception exception, int statusCode) {
		BaseResponse baseResponse = BaseResponse.builder()
				.statusCode(statusCode)
				.isError(true)
				.message(exception.getLocalizedMessage())
				.build();

		return ResponseEntity.internalServerError().body(baseResponse);
	}
}