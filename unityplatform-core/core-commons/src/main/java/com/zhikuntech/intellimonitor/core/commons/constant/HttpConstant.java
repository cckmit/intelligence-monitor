package com.zhikuntech.intellimonitor.core.commons.constant;


public interface HttpConstant {
    // 响应请求成功code
    Integer HTTP_RES_CODE_200 = 200;
    // 系统错误
    Integer HTTP_RES_CODE_500 = 500;

    Integer RPC_RESULT_SUCCESS = 200;

    String UPM_RESULT_SUCCESS = "0";

    Integer UPM_RESULT_ERROR = -2;

    Integer UPM_SESSION_ID_EXPIRE = 100001;

    //upm 调用接口成功
    Integer UPM_HTTP_CODE_SUCCESS = 0;

    //sftp异常
    Integer SFTP_FAILURE = 90001;
}
