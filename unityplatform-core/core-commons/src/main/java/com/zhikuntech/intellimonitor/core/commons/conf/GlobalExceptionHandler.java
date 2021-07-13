package com.zhikuntech.intellimonitor.core.commons.conf;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.core.commons.exception.GetSnapshotsException;
import com.zhikuntech.intellimonitor.core.commons.exception.RemoteInterfaceCallException;
import com.zhikuntech.intellimonitor.core.commons.exception.SubscribeGoldenException;
import com.zhikuntech.intellimonitor.core.commons.exception.UserNotLoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 18:29
 * @Description 全局异常处理
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public BaseResponse handleAll(Exception ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(GetSnapshotsException.class)
    public BaseResponse handleGetSnapshotsException(GetSnapshotsException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.GOLDEN_GETSNAPSHOTS_FAILED,ex.getMessage());
    }

    @ExceptionHandler(SubscribeGoldenException.class)
    public BaseResponse handSubscribeGoldenException(SubscribeGoldenException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.GOLDEN_SUBSCRIBESNAPSHOTS_FAILED,ex.getMessage());
    }

    @ExceptionHandler(UserNotLoginException.class)
    public BaseResponse handUserNotLoginException(UserNotLoginException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.USER_NOT_LOGIN_EXCEPTION,ex.getMessage());
    }

    @ExceptionHandler(RemoteInterfaceCallException.class)
    public BaseResponse handRemoteInterfaceCallException(RemoteInterfaceCallException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.REMOTE_INTERFACE_CALL_EXCEPTION,ex.getMessage());
    }


    @ExceptionHandler(NullPointerException.class)
    public BaseResponse handNullPointerException(NullPointerException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.NULL_POINTER_EXCEPTION,ex.getMessage());
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public BaseResponse handArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION,ex.getMessage());
    }

    @ExceptionHandler(ClassCastException.class)
    public BaseResponse handClassCastException(ClassCastException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.CLASS_CAST_EXCEPTION,ex.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public BaseResponse handSQLException(SQLException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.SQL_EXCEPTION,ex.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    public BaseResponse handArithmeticException(ArithmeticException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.ARITHMETIC_EXCEPTION,ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResponse handIllegalArgumentException(IllegalArgumentException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.ILLEGAL_ARGUMENT_EXCEPTION,ex.getMessage());
    }

    @ExceptionHandler(IllegalAccessException.class)
    public BaseResponse handIllegalAccessException(IllegalAccessException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.ILLEGAL_ACCESS_EXCEPTION,ex.getMessage());
    }

    @ExceptionHandler(StackOverflowError.class)
    public BaseResponse handStackOverflowError(StackOverflowError ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.STACK_OVER_FLOW_ERROR,ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public BaseResponse handIllegalStateException(IllegalStateException ex){
        LOGGER.error("异常!, msg:[{}], stack:[{}]", ex.getMessage(), ex);
        return BaseResponse.failure(ResultCode.ILLEGAL_STATE_EXCEPTION,ex.getMessage());
    }
}
