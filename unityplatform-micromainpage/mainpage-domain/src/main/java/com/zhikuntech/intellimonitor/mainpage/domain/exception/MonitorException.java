package com.zhikuntech.intellimonitor.mainpage.domain.exception;


import com.zhikuntech.intellimonitor.mainpage.domain.base.ResultCode;

public class MonitorException {

    public static class BadException extends BaseException {
        private static final long serialVersionUID = -8838868325494044461L;
        public BadException(ResultCode code) {
            super(code);
        }
    }

}
