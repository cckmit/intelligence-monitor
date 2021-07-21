package com.zhikuntech.intellimonitor;

import com.zhikuntech.intellimonitor.FanScadaApplication;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailOneService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanDetailDataVO;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanModelDataVO;

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
public class FanDetailOneServiceTest {
    @Autowired
    private FanDetailOneService detailOneService;
    //风机编号，
    String num = "44";
    @Test
    public void getData() {
        BaseResponse<FanModelDataVO> data = detailOneService.getData(num);
        System.out.println(data);
    }
}