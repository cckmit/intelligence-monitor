package com.zhikuntech.intellimonitor;

import com.rtdb.api.model.RtdbData;
import com.rtdb.api.util.DateUtil;
import com.rtdb.model.DoubleData;
import com.rtdb.service.impl.HistorianImpl;
import com.rtdb.service.impl.ServerImpl;
import com.rtdb.service.impl.ServerImplPool;
import com.rtdb.service.inter.Historian;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.InjectPropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author 滕楠
 * @className GoldenTest
 * @create 2021/7/7 10:35
 **/
@Slf4j
public class GoldenTest2 {


    public static void main(String[] args) throws Exception{
        ServerImplPool serverImplPool = new ServerImplPool("1.117.33.103", 6327, "sa", "golden", 25, 50);
        ServerImpl serverImpl = serverImplPool.getServerImpl();
        HistorianImpl his = new HistorianImpl(serverImpl);
        int id = 1;
        Date dateStart = DateUtil.stringToDate("2021-07-02 15:40:00");
        Date dateEnd = DateUtil.stringToDate("2021-07-02 15:50:00");

        long s = System.currentTimeMillis();
        //该标签点这段时间内的存储值数量
        int count = his.archivedValuesCount(id, dateStart, dateEnd);
        //该标签点这段时间内的真实存储值数量
        int realCount = his.archivedValuesRealCount(id, dateStart, dateEnd);

        List<RtdbData> archivedValues = his.getArchivedValues(id, realCount, dateStart, dateEnd);
        long e = System.currentTimeMillis();
        log.info(count + "_______" + realCount);
        log.info(archivedValues.size() + "  条记录,用时 : " + (e - s) + "  ms");
        System.out.println(archivedValues.toString());
    }
}