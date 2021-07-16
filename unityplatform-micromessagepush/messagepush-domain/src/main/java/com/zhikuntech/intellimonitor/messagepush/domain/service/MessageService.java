package com.zhikuntech.intellimonitor.messagepush.domain.service;


/**
 * @author 滕楠
 * @className MessageService
 * @create 2021/7/8 11:39
 **/
public interface MessageService {

    Boolean messagePush(String messageBody,Integer type);
}