package com.zhikuntech.intellimonitor.mainpage.domain.golden;

import com.rtdb.api.callbackInter.RSDataChangeEx;
import com.rtdb.api.model.ValueData;
import com.rtdb.api.util.DateUtil;
import com.rtdb.enums.DataSort;
import com.rtdb.enums.RtdbHisMode;
import com.rtdb.model.SearchCondition;
import com.rtdb.service.impl.*;
import com.rtdb.service.inter.Base;
import com.rtdb.service.inter.Historian;
import com.rtdb.service.inter.Snapshot;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * @author 代志豪
 * 2021/6/7 10:50
 */
@Component
public class GoldenUtil {

    /**
     * 庚顿数据库网络连接接口
     */
    private ServerImpl server;

    /**
     * 庚顿数据库快照数据接口
     */
    private Snapshot snap;

    /**
     * 庚顿数据库历史数据接口
     */
    private Historian historian;

    /**
     * 庚顿数据库基础接口
     */
    private Base base;

    /**
     * 庚顿数据库连接池
     */
    private static final ServerImplPool SERVER_IMPL_POOL = new ServerImplPool("47.114.57.32", 6327, "sa", "golden", 5, 10);


    /**
     * 获取表内ids
     * @param tableName 表名
     * @return int[]
     */
    public int[] getIds(String tableName) throws Exception {
        server = SERVER_IMPL_POOL.getServerImpl();
        base = new BaseImpl(server);
        //获取当前表容量
        int count = base.getTableSizeByName(tableName);
        SearchCondition s = new SearchCondition();
        s.setTablemask(tableName);
        return base.search(s, count, DataSort.SORT_BY_ID);
    }

    /**
     * 获取庚顿推送快照(实时)数据
     *
     * @param param          用户自定义参数,主要用于区别客户端
     * @param rsDataChangeEx 庚顿数据更新时触发，进行相关处理
     * @throws Exception
     */
    public void subscribeSnapshots(Object param, int[] ids, RSDataChangeEx rsDataChangeEx) throws Exception {
        server = SERVER_IMPL_POOL.getServerImpl();
        snap = new SnapshotImpl(server);

        //errors.length必须与count相等
        int[] errors = new int[ids.length];
        snap.subscribeSnapshotsEx(param, ids, rsDataChangeEx, errors);
//        RSDataChangeEx rs = (p, datas) -> {
//            //todo 后续处理
//            System.out.print(param + ": \t" + datas.length);
//            for (RtdbData data : datas) {
//                System.out.print(",id: " + data.getId() + " :: " + data.getValue() + " :: "
//                        + DateUtil.dateToString(data.getDate()) + " ");
//            }
//            System.out.println(" end");
//        };
    }

    /**
     * 获取指定时间的数据
     * RtdbHisMode 如果指定时间没有数据
     * RTDB_NEXT(0), 返回下一个最近的
     * RTDB_PREVIOUS(1), 返回前一个最近的
     * RTDB_EXACT(2), 抛出异常
     * RTDB_INTER(3); 取指定时间的内插值数据
     */
    public double getFloat(int id, String dateTime) throws Exception {
        server = SERVER_IMPL_POOL.getServerImpl();
        historian = new HistorianImpl(server);
        Date date = DateUtil.stringToDate(dateTime);
        return historian.getFloatSingleValue(id, date, RtdbHisMode.RTDB_PREVIOUS).getValue();
    }

    public double getInteger(int id, String dateTime) throws Exception {
        server = SERVER_IMPL_POOL.getServerImpl();
        historian = new HistorianImpl(server);
        Date date = DateUtil.stringToDate(dateTime);
        return historian.getIntSingleValue(id, date, RtdbHisMode.RTDB_PREVIOUS).getValue();
    }

    /**
     * 获取指定id当前最新快照信息
     *
     * @param ids 指定id集合
     * @return 指定id集合最新快照信息
     */
    public List<ValueData> getSnapshots(int[] ids) throws Exception {
        server = SERVER_IMPL_POOL.getServerImpl();
        snap = new SnapshotImpl(server);
        return snap.getSnapshots(ids);
    }
}
