package com.zhikuntech.intellimonitor;

import com.zhikuntech.intellimonitor.integratedautomation.domain.service.LandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 代志豪
 * 2021/7/20 15:20
 */
@SpringBootTest(classes = IntegratedAutomationApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LandServiceImplTest {

    @Autowired
    private LandService landService;

    @Test
    public void subscribe() throws Exception{
        landService.subscribe("land",new int[]{1});
    }
}