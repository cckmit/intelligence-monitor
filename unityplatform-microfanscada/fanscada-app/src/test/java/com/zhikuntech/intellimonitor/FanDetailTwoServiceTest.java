package com.zhikuntech.intellimonitor;


import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailTwoService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanDetailDataVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author： DAI
 * @date： Created in 2021/7/20 14:09
 */
@SpringBootTest(classes = FanScadaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class FanDetailTwoServiceTest {

    @Autowired
    private FanDetailTwoService detailTwoService;

    // 风机编号
    String number = "44";

    @Test
    public void getData() {
        BaseResponse<FanDetailDataVO> data = detailTwoService.getData(number);
        System.out.println(data);
    }

    @Test
    public void getFanParameterByNumber() {
        System.out.println(detailTwoService.getFanParameterByNumber(number));
    }
}
