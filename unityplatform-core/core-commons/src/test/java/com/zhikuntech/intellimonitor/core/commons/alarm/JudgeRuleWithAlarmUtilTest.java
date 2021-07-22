package com.zhikuntech.intellimonitor.core.commons.alarm;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;

/**
 * @Author 杨锦程
 * @Date 2021/7/8 16:38
 * @Description 测试警告判断工具类
 * @Version 1.0
 */
@Slf4j
public class JudgeRuleWithAlarmUtilTest {
    String valueStr = "";
    AlarmRuleDTO alarmRuleDTO = null;

    @After
    public void after(){
        AlarmResultDTO resultDTO = JudgeRuleWithAlarmUtil.process(valueStr, alarmRuleDTO);
        log.info(resultDTO.toString());
    }

    @Test
    public void test1(){
//        valueStr = "30";
//        alarmRuleDTO = AlarmRuleDTO.builder().alarmRange("30,80").preWarningRangeLevelOne("5,95").preWarningRangeLevelTwe("10,90").build();
        valueStr = "22.5";
        alarmRuleDTO = AlarmRuleDTO.builder().alarmRange("(11,22)").preWarningRangeLevelOne("2,3").preWarningRangeLevelTwe("44,55").build();
    }

    @Test
    public void test2(){
        valueStr = "29";
        alarmRuleDTO = AlarmRuleDTO.builder().alarmRange("(-∞,30)∪(80,+∞)").preWarningRangeLevelOne("40,50").preWarningRangeLevelTwe("35,55").build();
    }
}
