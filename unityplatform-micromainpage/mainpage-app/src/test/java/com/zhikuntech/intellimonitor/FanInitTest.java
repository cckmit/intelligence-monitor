package com.zhikuntech.intellimonitor;

import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGolden;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGoldenQuery;
import com.zhikuntech.intellimonitor.mainpage.domain.service.BackendToGoldenService;
import com.zhikuntech.intellimonitor.mainpage.domain.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 代志豪
 * 2021/6/18 10:44
 */
@SpringBootTest(classes = MainPageApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class FanInitTest {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BackendToGoldenService backendToGoldenService;

    @Test
    public void test() {
//        List<String> list =new ArrayList<>();
//        for (int i = 1; i < 64; i++) {
////            list.add(FanConstant.GOLDEN_ID_POWER+i);
////            redisUtil.setString(FanConstant.GOLDEN_ID_POWER+i,"13");
//            for (int j = 1; j <15; j++) {
//                redisUtil.setString(FanConstant.GOLDEN_ID + j+"_"+i,j+"");
//            }
//
//        }
////        redisUtil.delKeys(list);
////        Set<String> keys = redisUtil.getKeys(FanConstant.GOLDEN_ID_POWER);
////        keys.forEach(System.out::println);
//        redisUtil.set(FanConstant.DAILY_POWER_ALL, 0.0);
//        redisUtil.set(FanConstant.MONTHLY_POWER_ALL, 0.0);
//        redisUtil.set(FanConstant.ANNUAL_POWER_ALL, 0.0);
//        redisUtil.set(FanConstant.DAILY_ONLINE_ALL, 0.0);
//        redisUtil.set(FanConstant.MONTHLY_ONLINE_ALL, 0.0);
//        redisUtil.set(FanConstant.ANNUAL_ONLINE_ALL, 0.0);

        BackendToGoldenQuery query = new BackendToGoldenQuery();
        List<BackendToGolden> list = backendToGoldenService.getGoldenIdByBackendIdOrNumber(query);
        for (int i = 1; i < 64; i++) {
            for (BackendToGolden e : list) {
                redisUtil.setString(FanConstant.GOLDEN_ID + e.getBackendId() + "_" + i, e.getGoldenId().toString());
            }
        }

    }



}
