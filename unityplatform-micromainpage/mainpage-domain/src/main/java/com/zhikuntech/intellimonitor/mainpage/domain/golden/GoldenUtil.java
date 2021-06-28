package com.zhikuntech.intellimonitor.mainpage.domain.golden;

import com.rtdb.api.callbackInter.RSDataChange;
import com.rtdb.api.model.ValueData;
import com.rtdb.api.util.DateUtil;
import com.rtdb.enums.DataSort;
import com.rtdb.enums.RtdbHisMode;
import com.rtdb.model.SearchCondition;
import com.rtdb.service.impl.*;
import com.rtdb.service.inter.Historian;
import com.rtdb.service.inter.Snapshot;
import com.zhikuntech.intellimonitor.mainpage.domain.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.SocketException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author 代志豪
 * 2021/6/7 10:50
 */
@Component
@Slf4j
public class GoldenUtil {

    @Value("${golden.ip}")
    private String ip;

    @Value("${golden.port}")
    private Integer port;

    @Value("${golden.user}")
    private String user;

    @Value("${golden.password}")
    private String password;

    @Value("${golden.poolSize}")
    private Integer poolSize;

    @Value("${golden.maxSize}")
    private Integer maxSize;

    private ServerImplPool pool;

    /**
     * 初始化庚顿数据库连接池
     */
    @PostConstruct
    private void init() {
        pool = new ServerImplPool(ip, port, user, password, poolSize, maxSize);
    }

    private static ConcurrentHashMap<String, ServerImpl> servers = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, Snapshot> snaps = new ConcurrentHashMap<>();

    /**
     * 获取表内ids
     *
     * @param tableName 表名
     * @return int[]
     */
    public int[] getIds(String tableName) throws Exception {
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
    public void subscribeSnapshots(String username, int[] ids, RSDataChange rsDataChange) throws Exception {
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
    public void subscribeSnapshots(String username, String[] tagNames, RSDataChange rsDataChange) throws Exception {
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
    public void cancel(String username) {
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
    }

    /**
     * 取消未推送数据的连接
     */
    private void cancel() {
        servers.keySet().forEach(e -> {
            if (!WebSocketServer.clients.containsKey(e)) {
                cancel(e);
            }
        });
    }

    /**
     * 取消所有连接
     */
    public void cancelAll() {
        servers.keySet().forEach(this::cancel);
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
    public double getFloat(int id, String dateTime) throws Exception {
        check();
        ServerImpl server = pool.getServerImpl();
        server.setTimeOut(5);
        Historian historian = new HistorianImpl(server);
        Date date = DateUtil.stringToDate(dateTime);
        double value = historian.getFloatSingleValue(id, date, RtdbHisMode.RTDB_PREVIOUS).getValue();
        server.close();
        return value;
    }

    public int getInteger(int id, String dateTime) throws Exception {
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
     * 获取指定id当前最新快照信息
     *
     * @param ids 指定id集合
     * @return 指定id集合最新快照信息
     */
    public List<ValueData> getSnapshots(int[] ids) throws Exception {
        check();
        ServerImpl server = pool.getServerImpl();
        server.setTimeOut(5);
        Snapshot snap = new SnapshotImpl(server);
        List<ValueData> snapshots = snap.getSnapshots(ids);
        snap.close();
        server.close();
        return snapshots;
    }

    private void check() throws Exception {
        if (pool.getRealSize() == maxSize) {
            throw new Exception("golden数据库连接池已满，连接失败！");
        }
        if (pool.getRealSize() > maxSize / 4) {
            cancel();
        }
    }

    public ServerImplPool getPool() {
        return this.pool;
    }

    public ConcurrentHashMap<String, ServerImpl> getServer() {
        return servers;
    }

    public List<ValueData> getSnapshotsTest(String username) throws Exception {
        int[] ids = {1};
        Snapshot snap = snaps.get(username);
        List<ValueData> snapshots = snap.getSnapshots(ids);
        log.info(snapshots.get(0).getValue()+"");
        return snapshots;
    }
}
