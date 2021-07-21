package com.zhikuntech.intellimonitor.fanscada.domain.test;

import com.rtdb.model.IntData;
import com.rtdb.service.impl.ServerImpl;
import com.rtdb.service.impl.ServerImplPool;
import com.rtdb.service.impl.SnapshotImpl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author 滕楠
 * @className GoldenTest
 * @create 2021/7/5 14:48
 **/
public class GoldenTest {
    public static void main(String[] args) {
        try {
            ServerImplPool pool = new ServerImplPool("1.117.33.103", 6327, "sa", "golden", 25, 50);
            ServerImpl serverImpl = pool.getServerImpl();
            SnapshotImpl snapshot = new SnapshotImpl(serverImpl);
            for (int i = 0; i < 1000; i++) {
                List<IntData> list = new ArrayList<>();
                IntData intData = new IntData();
                intData.setDateTime(new Date());
                intData.setId(177);
                intData.setValue(9.9);
                Random random = new Random();
                int a = random.nextInt();
                intData.setState(a);
                Thread.sleep(999);
                System.out.println(intData);

                list.add(intData);
                snapshot.putIntSnapshots(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
