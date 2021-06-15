package com.zhikuntech.intellimonitor.fanscada.domain.websocket;

import com.rtdb.api.model.RtdbData;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 代志豪
 * @date 2021-06-07
 */
@Component
@Slf4j
public class WebSocketTest {

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private GoldenUtil goldenUtil;

    //    @Scheduled(cron = "*/5 * * * * ?")
    public void send(String user) throws Exception {
//        Random random=  new Random();
//        int i = random.nextInt(10);
//        log.info(Integer.toString(i));
        int[] ids = goldenUtil.getIds("fan");
        goldenUtil.subscribeSnapshots(ids, (datas) -> {
            if (webSocketServer.getClients().containsKey(user)) {
                StringBuilder sb = new StringBuilder();
                for (RtdbData data : datas) {
                    sb.append(data.getValue().toString());
                }
                webSocketServer.sendMessage(sb.toString(),user);
            }
        });
    }
}
