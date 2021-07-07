package com.zhikuntech.intellimonitor;

import com.rtdb.model.DoubleData;
import com.rtdb.service.impl.HistorianImpl;
import com.rtdb.service.impl.ServerImpl;
import com.rtdb.service.impl.ServerImplPool;
import com.rtdb.service.inter.Historian;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author 滕楠
 * @className GoldenTest
 * @create 2021/7/7 10:35
 **/
public class GoldenTest {


    public static void main(String[] args) {
        ServerImplPool serverImplPool = new ServerImplPool("1.117.33.103", 6327, "sa", "golden", 25, 50);
        try {
            ServerImpl serverImpl = serverImplPool.getServerImpl();
            Historian historian = new HistorianImpl(serverImpl);
            Date startDate = new Date(121, Calendar.AUGUST, 6);
            Date endDate = new Date(121, Calendar.AUGUST, 7);

            List<DoubleData> doubleArchivedValues = historian.getDoubleArchivedValues(1, 10, startDate, endDate);
            System.out.println(doubleArchivedValues.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}