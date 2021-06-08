package com.zhikuntech.intellimonitor.mainpage.domain.websocket;

import com.rtdb.api.model.RtdbData;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
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
        int[] ids = {1, 2, 3, 4, 5, 6, 7, 8};
        goldenUtil.subscribeSnapshots(user, ids, (p, datas) -> {
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
