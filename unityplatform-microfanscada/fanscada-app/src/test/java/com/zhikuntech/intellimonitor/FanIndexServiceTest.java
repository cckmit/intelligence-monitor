package com.zhikuntech.intellimonitor;

import com.zhikuntech.intellimonitor.FanScadaApplication;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanIndexService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName: intelligence-monitor
 * @Description:
 * @Author: 沈slk123
 * @CreateDate: 2021/7/20
 * @Version:
 */
@SpringBootTest(classes = FanScadaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class FanIndexServiceTest {
    @Autowired
    private FanIndexService fanIndexService;

    @Test
    public void getFanBaseInfoList() {
        System.out.println(fanIndexService.getFanBaseInfoList());
    }

    @Test
    public void testGetFanBaseInfoList() {

    }
}