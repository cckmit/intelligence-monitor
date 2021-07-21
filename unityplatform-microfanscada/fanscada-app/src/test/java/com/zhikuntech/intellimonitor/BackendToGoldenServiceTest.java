package com.zhikuntech.intellimonitor;

import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGolden;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQuery;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQueryList;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.GoldenIdQuery;
import com.zhikuntech.intellimonitor.fanscada.domain.service.BackendToGoldenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author： DAI
 * @date： Created in 2021/7/20 14:35
 */
@SpringBootTest(classes = FanScadaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class BackendToGoldenServiceTest {

    @Autowired
    private BackendToGoldenService backendToGoldenService;

    BackendToGoldenQuery query = new BackendToGoldenQuery();
    BackendToGoldenQueryList queryList = new BackendToGoldenQueryList();
    GoldenIdQuery goldenIdQuery = new GoldenIdQuery();

    @Test
    public void getGoldenIdByBackendIdOrNumber() {

        // 通过风机编号查询goldenId
        query.setNumber(44);
        List<Integer> goldenIdList1 = backendToGoldenService.getGoldenIdByBackendIdOrNumber(query);
        System.out.println("++++++++++++++++++++++");
        System.out.println(goldenIdList1);
        System.out.println();

        // 通过backendId查询goldenId
        query = new BackendToGoldenQuery();
        query.setBackendId(192);
        List<Integer> goldenIdList2 = backendToGoldenService.getGoldenIdByBackendIdOrNumber(query);
        System.out.println("++++++++++++++++++++++");
        System.out.println(goldenIdList2);
        System.out.println();
    }

    @Test
    public void listGoldenIdByBackendIdOrNumber() {
        // 通过风机编号批量查询goldenId
        List<Integer> numberList = Arrays.asList(1, 44);
        queryList.setNumberList(numberList);
        List<Integer> goldenIdList1 = backendToGoldenService.listGoldenIdByBackendIdOrNumber(queryList);
        System.out.println("++++++++++++++++++++++");
        System.out.println(goldenIdList1);
        System.out.println();

        // 通过backendId批量查询goldenId
        List<Integer> backendIdList= Arrays.asList(179, 192);
        queryList = new BackendToGoldenQueryList();
        queryList.setBackendIdList(backendIdList);
        List<Integer> goldenIdList2 = backendToGoldenService.listGoldenIdByBackendIdOrNumber(queryList);
        System.out.println("++++++++++++++++++++++");
        System.out.println(goldenIdList2);
        System.out.println();
    }

    @Test
    public void getGoldenIdByNumberAndId() {
        List<Integer> backendIdList = Arrays.asList(179, 192);
        goldenIdQuery.setDataIds(backendIdList);
        int[] ints = backendToGoldenService.getGoldenIdByNumberAndId(goldenIdQuery);
        System.out.println("++++++++++++++++++++++");
        System.out.println(Arrays.toString(ints));
    }

    @Test
    public void selectList() {
        List<Integer> backendIdList = Arrays.asList(179, 192);
        List<BackendToGolden> list = backendToGoldenService.selectList(backendIdList);
        System.out.println("++++++++++++++++++++++");
        System.out.println(list);
    }

    @Test
    public void getListByBackendId() {
        Integer backendId = 192;
        List<BackendToGolden> list = backendToGoldenService.getListByBackendId(backendId);
        System.out.println("++++++++++++++++++++++");
        System.out.println(list);
    }
}
