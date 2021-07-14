package com.zhikuntech.intellimonitor.core.commons.exception;

import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 18:31
 * @Description 获取指定id当前最新快照信息时异常
 * @Version 1.0
 */
public class GetSnapshotsException extends BaseException {
    public GetSnapshotsException(ResultCode code) {
        super(code);
    }

    public GetSnapshotsException(ResultCode code, String msg) {
        super(code, msg);
    }

    @Override
    public ResultCode getCode() {
        return super.getCode();
    }
}
