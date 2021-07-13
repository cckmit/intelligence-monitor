package com.zhikuntech.intellimonitor.core.commons.exception;

import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;

/**
 * @Author 杨锦程
 * @Date 2021/6/9 16:08
 * @Description 订阅庚顿点位异常
 * @Version 1.0
 */
public class SubscribeGoldenException extends BaseException {
    public SubscribeGoldenException(ResultCode code) {
        super(code);
    }

    public SubscribeGoldenException(ResultCode code, String msg) {
        super(code, msg);
    }

    @Override
    public ResultCode getCode() {
        return super.getCode();
    }
}
