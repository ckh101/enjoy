package com.ckh.enjoy.utils;

import lombok.Data;

@Data
public class ApiResponse {
	private int status;
	private String msg;
	private Object data;

	public static ApiResponse result(int code, String msg, Object data) {
		ApiResponse ret = new ApiResponse();
		ret.setStatus(code);
		ret.setMsg(msg);
		ret.setData(data);

		return ret;
	}

	public static ApiResponse ok() {
		return ok(null);
	}

	public static ApiResponse ok(Object data) {
		return result(0, "操作成功", data);
	}

	public static ApiResponse error(String message) {
		return result(-1, message, null);
	}
}
