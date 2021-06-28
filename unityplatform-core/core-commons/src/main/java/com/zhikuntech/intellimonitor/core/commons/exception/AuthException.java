package com.zhikuntech.intellimonitor.core.commons.exception;

import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;

/**
 * @Author 杨锦程
 * @Date 2021/6/22 14:19
 * @Description 权限异常
 * @Version 1.0
 */
public class AuthException extends BaseException{
    public AuthException(ResultCode code) {
        super(code);
    }

    public AuthException(ResultCode code, String msg) {
        super(code, msg);
    }

    @Override
    public ResultCode getCode() {
        return super.getCode();
    }
}
