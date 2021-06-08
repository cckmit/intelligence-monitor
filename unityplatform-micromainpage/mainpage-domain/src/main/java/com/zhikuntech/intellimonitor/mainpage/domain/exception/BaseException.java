package com.zhikuntech.intellimonitor.mainpage.domain.exception;


import com.zhikuntech.intellimonitor.mainpage.domain.base.ResultCode;

public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = -6261552376597235939L;
    private final ResultCode code;

    public BaseException(ResultCode code) {
        super(code.msg());
        this.code = code;
    }

    public BaseException(ResultCode code, String msg) {
        super(msg);
        code.setMsg(msg);
        this.code = code;
    }

    public ResultCode getCode(){
        return this.code;
    }
}
