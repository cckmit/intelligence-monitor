package com.zhikuntech.intellimonitor.messagepush.domain.controller;

import com.zhikuntech.intellimonitor.messagepush.domain.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 滕楠
 * @className MessageController
 * @create 2021/7/16 14:39
 **/
@RestController("messagePush")
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("sendMessage")
    public Boolean sendMessage(Object object, Integer type) {

        messageService.messagePush(object.toString(),type);
        return true;
    }
}