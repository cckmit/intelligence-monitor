package com.zhikuntech.intellimonitor.core.commons.exception;

import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import lombok.Data;

/**
 * @Author 杨锦程
 * @Date 2021/6/17 14:34
 * @Version 1.0
 * @Describe SFTP异常类
 */
@Data
public class CodeException extends RuntimeException{
    private ResultCode code;
    private String detail;

    public CodeException(ResultCode code,String detail) {
        super(detail == null ? code.msg() : detail);
        this.code = code;
        this.detail = detail;
    }

    public CodeException(ResultCode code) {
        this(code,null);
    }

    public CodeException(String message, Throwable cause){
        super(message,cause);
    }
}
