package com.zhikuntech.intellimonitor.core.commons.golden;

import com.rtdb.api.callbackInter.RSDataChange;
import com.rtdb.api.exception.EncodePacketErrorException;
import com.rtdb.api.exception.NoAuthorityException;
import com.rtdb.api.model.RtdbData;
import com.rtdb.api.model.ValueData;
import com.rtdb.api.util.DateUtil;
import com.rtdb.enums.DataSort;
import com.rtdb.enums.RtdbHisMode;
import com.rtdb.model.MinPoint;
import com.rtdb.model.ErrorParse;
import com.rtdb.model.SearchCondition;
import com.rtdb.service.impl.*;
import com.rtdb.service.inter.Base;
import com.rtdb.service.inter.Historian;
import com.rtdb.service.inter.Snapshot;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author 代志豪
 * 2021/6/7 10:50
 */
@Slf4j
public class GoldenUtil {

    public static ServerImplPool pool;

    public static Base base;

    /**
     * 初始化庚顿数据库连接池
     */
    public static void init(String ip, int port, String user, String passWord, int poolSize, int maxSize) {
        pool = new ServerImplPool(ip, port, user, passWord, poolSize, maxSize);
    }

    public static ConcurrentHashMap<String, ServerImpl> servers = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, Snapshot> snaps = new ConcurrentHashMap<>();

    /**
     * 获取表内ids
     *
     * @param tableName 表名
     * @return int[]
     */
    public static int[] getIds(String tableName) throws Exception {
        check();
        ServerImpl server = pool.getServerImpl();
        server.setTimeOut(5);
        BaseImpl base = new BaseImpl(server);
        //获取当前表容量
        int count = base.getTableSizeByName(tableName);
        SearchCondition s = new SearchCondition();
        s.setTablemask(tableName);
        int[] search = base.search(s, count, DataSort.SORT_BY_ID);
        server.close();
        return search;
    }

    /**
     * 获取庚顿推送快照(实时)数据
     *
     * @param ids          根据id集合查询
     * @param rsDataChange 庚顿数据更新时触发，进行相关处理
     * @throws Exception
     */
    public static void subscribeSnapshots(String username, int[] ids, RSDataChange rsDataChange) throws Exception {
        check();
        ServerImpl server = pool.getServerImpl();
        Snapshot snap = new SnapshotImpl(server);
        servers.put(username, server);
        snaps.put(username, snap);
        snap.subscribeSnapshots(ids, rsDataChange);
//        RSDataChange rs = (datas) -> {
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
     * @param tagNames 更具标签的名称查询
     */
    public static void subscribeSnapshots(String username, String[] tagNames, RSDataChange rsDataChange) throws Exception {
        check();
        ServerImpl server = pool.getServerImpl();
        Snapshot snap = new SnapshotImpl(server);
        servers.put(username, server);
        snap.subscribeSnapshots(tagNames, rsDataChange);
//        RSDataChange rs = (datas) -> {
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
     * 取消订阅
     */
    public static void cancel(String username) {
        Snapshot snap = snaps.get(username);
        ServerImpl server = servers.get(username);
        try {
            snap.cancelSubscribeSnapshots();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        try {
            server.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        snaps.remove(username);
        servers.remove(username);
        if (servers.isEmpty()) {
            pool.closePool();
        }
    }

    /**
     * 取消所有连接
     */
    public static void cancelAll() {
        servers.keySet().forEach(GoldenUtil::cancel);
        pool.closePool();
    }

    /**
     * 获取指定时间的数据
     * RtdbHisMode 如果指定时间没有数据
     * RTDB_NEXT(0), 返回下一个最近的
     * RTDB_PREVIOUS(1), 返回前一个最近的
     * RTDB_EXACT(2), 抛出异常
     * RTDB_INTER(3); 取指定时间的内插值数据
     */
    public static double getFloat(int id, String dateTime) throws Exception {
        check();
        ServerImpl server = pool.getServerImpl();
        server.setTimeOut(5);
        Historian historian = new HistorianImpl(server);
        Date date = DateUtil.stringToDate(dateTime);
        double value = historian.getFloatSingleValue(id, date, RtdbHisMode.RTDB_PREVIOUS).getValue();
        server.close();
        return value;
    }

    public static int getInteger(int id, String dateTime) throws Exception {
        check();
        ServerImpl server = pool.getServerImpl();
        server.setTimeOut(5);
        Historian historian = new HistorianImpl(server);
        Date date = DateUtil.stringToDate(dateTime);
        double value = historian.getIntSingleValue(id, date, RtdbHisMode.RTDB_PREVIOUS).getValue();
        server.close();
        return (int) value;
    }

    /**
     * 获取指定标签点一段时间内的历史存储值
     * @param id          标签点id
     * @param dateStart   开始时间
     * @param dateEnd     结束时间
     */
    public static List<RtdbData> getArchivedValues(int id, Date dateStart, Date dateEnd) throws Exception {
        ServerImpl serverImpl = pool.getServerImpl();
        HistorianImpl his = new HistorianImpl(serverImpl);
//        id = 1;
//        dateStart = DateUtil.stringToDate("2021-07-02 15:40:00");
//        dateEnd = DateUtil.stringToDate("2021-07-02 15:50:00");

        long s = System.currentTimeMillis();
        //该标签点这段时间内的存储值数量
        int count = his.archivedValuesCount(id, dateStart, dateEnd);
        //该标签点这段时间内的真实存储值数量
        int realCount = his.archivedValuesRealCount(id, dateStart, dateEnd);

        List<RtdbData> archivedValues = null;
        try {
            archivedValues = his.getArchivedValues(id, realCount, dateStart, dateEnd);
            long e = System.currentTimeMillis();
            log.info(count + "_______" + realCount);
            log.info(archivedValues.size() + "  条记录,用时 : " + (e - s) + "  ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return archivedValues;
    }


    /**
     * 获取指定标签点指定时间的历史存储值
     * @param ids          标签点id
     * @param datetime     时间
     */
    public static List<RtdbData> getCrossSectionValues(int[] ids,Date datetime) throws UnsupportedEncodingException, NoAuthorityException, IOException,
            EncodePacketErrorException, Exception {
        check();
        ServerImpl serverImpl = pool.getServerImpl();
        serverImpl.setTimeOut(5);
        HistorianImpl his = new HistorianImpl(serverImpl);
        List<RtdbData> list = his.getCrossSectionValues(ids, datetime, RtdbHisMode.RTDB_NEXT);
        System.out.println(list.size());
        his.close();
        serverImpl.close();
        return list;
    }

    /**
     * 获取指定id当前最新快照信息
     *
     * @param ids 指定id集合
     * @return 指定id集合最新快照信息
     */
    public static List<ValueData> getSnapshots(int[] ids) throws Exception {
        check();
        ServerImpl server = pool.getServerImpl();
        server.setTimeOut(5);
        Snapshot snap = new SnapshotImpl(server);
        List<ValueData> snapshots = snap.getSnapshots(ids);
        snap.close();
        server.close();
        return snapshots;
    }

    /**
     * 检查庚顿实际连接池
     */
    private static void check() throws Exception {
        if (servers.isEmpty()) {
            pool.closePool();
        }
        if (pool.getRealSize() == pool.getMaxSize()) {
            pool.closePool();
            throw new Exception("golden数据库连接池已满，连接失败！");
        }
    }

    /**
     * 根据 "表名.标签点名" 格式批量获取标签点数据 stms.stmsp0082
     * @param strings
     * @return
     */
    public static List<MinPoint> findPoints(String[] strings){
        ServerImpl serverImpl = null;
        try {
            serverImpl = pool.getServerImpl();
            base = new BaseImpl(serverImpl);
            List<MinPoint> list = base.findPoints(strings);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
