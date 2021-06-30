package com.zhikuntech.intellimonitor.core.commons.exception;

import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;

/**
 * @Author 杨锦程
 * @Date 2021/6/30 14:20
 * @Description 远程接口调用异常
 * @Version 1.0
 */
public class RemoteInterfaceCallException extends BaseException{
    public RemoteInterfaceCallException(ResultCode code) {
        super(code);
    }

    public RemoteInterfaceCallException(ResultCode code, String msg) {
        super(code, msg);
    }

    @Override
    public ResultCode getCode() {
        return super.getCode();
    }
}
