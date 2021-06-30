package com.zhikuntech.intellimonitor.mainpage.domain.conf;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.exception.RemoteInterfaceCallException;
import com.zhikuntech.intellimonitor.mainpage.domain.exception.GetSnapshotsException;
import com.zhikuntech.intellimonitor.mainpage.domain.exception.SubscribeGoldenException;
import com.zhikuntech.intellimonitor.mainpage.domain.exception.UserNotLoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 18:29
 * @Description 全局异常处理
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(GetSnapshotsException.class)
    public BaseResponse handleGetSnapshotsException(GetSnapshotsException getSnapshotsException){
        LOGGER.error(getSnapshotsException.getMessage());
        return BaseResponse.exception(getSnapshotsException);
    }

    @ExceptionHandler(SubscribeGoldenException.class)
    public BaseResponse handSubscribeGoldenException(SubscribeGoldenException ex){
        LOGGER.error(ex.getMessage());
        return BaseResponse.exception(ex);
    }

    @ExceptionHandler(UserNotLoginException.class)
    public BaseResponse handUserNotLoginException(UserNotLoginException ex){
        LOGGER.error(ex.getMessage());
        return BaseResponse.exception(ex);
    }

    @ExceptionHandler(RemoteInterfaceCallException.class)
    public BaseResponse handRemoteInterfaceCallException(RemoteInterfaceCallException ex){
        LOGGER.error(ex.getMessage());
        return BaseResponse.exception(ex);
    }
}
