package com.zhikuntech.intellimonitor.mainpage.domain.exception;

import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.core.commons.exception.BaseException;

/**
 * @Author 杨锦程
 * @Date 2021/6/9 16:16
 * @Description 用户未登录异常
 * @Version 1.0
 */
public class UserNotLoginException extends BaseException {
    public UserNotLoginException(ResultCode code) {
        super(code);
    }

    public UserNotLoginException(ResultCode code, String msg) {
        super(code, msg);
    }

    @Override
    public ResultCode getCode() {
        return super.getCode();
    }
}
