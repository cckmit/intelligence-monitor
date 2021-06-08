package com.zhikuntech.intellimonitor.mainpage.domain.base;

import com.zhikuntech.intellimonitor.mainpage.domain.exception.BaseException;
import lombok.Data;
import org.springframework.util.StringUtils;

import static com.zhikuntech.intellimonitor.mainpage.domain.base.ResultCode.SUCCESS;


@Data
public class BaseResponse<T> {

	/**
	 * 返回码
	 */
	private Integer code;
	/**
	 * 消息
	 */
	private String msg;
	/**
	 * 返回
	 */
	private T data;

	public BaseResponse() {

	}

	public BaseResponse(Integer code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static <U> BaseResponse<U> success(U data) {
		return of(SUCCESS, "", data);
	}

	public static <U> BaseResponse<U> failure(ResultCode resultCode) {
		return of(resultCode, "", null);
	}

	public static <U> BaseResponse<U> failure(ResultCode resultCode, String msg) {
		return of(resultCode, msg,null);
	}

	public static <U> BaseResponse<U> failure(ResultCode resultCode, String msg, U data) {
		return of(resultCode, msg ,data);
	}

	public static <U,E extends BaseException> BaseResponse<U> exception(E ex) {
		return failure(ex.getCode());
	}

	private static <U> BaseResponse<U> of(ResultCode resultCode, String msg, U data) {
		BaseResponse<U> result = new BaseResponse<>();
		result.code = resultCode.code();
		if (!StringUtils.isEmpty(msg)){
			result.setMsg(msg);
		} else {
			result.setMsg(resultCode.msg());
		}
		result.setData(data);
		return result;
	}

}
