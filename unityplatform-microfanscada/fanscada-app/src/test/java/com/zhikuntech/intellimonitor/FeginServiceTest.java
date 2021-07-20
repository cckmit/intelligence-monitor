package com.zhikuntech.intellimonitor;

import com.zhikuntech.intellimonitor.fanscada.domain.service.FeginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author： DAI
 * @date： Created in 2021/7/20 16:39
 */
@SpringBootTest(classes = FanScadaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class FeginServiceTest {

    @Autowired
    private FeginService feginService;


    @Test
    public void getPowerSum() {
        System.out.println(feginService.getPowerSum());
    }
}
