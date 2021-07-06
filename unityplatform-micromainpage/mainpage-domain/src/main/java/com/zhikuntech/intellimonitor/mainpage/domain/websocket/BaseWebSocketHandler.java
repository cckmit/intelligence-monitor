package com.zhikuntech.intellimonitor.mainpage.domain.websocket;


import javax.websocket.Session;
import java.util.Set;

/**
 * @author 代志豪
 * 2021/7/6 11:57
 */
public interface BaseWebSocketHandler {

    /**
     * 连接建立成功调用的方法
     */
    void onOpen(String username);

    /**
     * 连接关闭调用的方法
     */
    void onClose(String username);

    /**
     * 收到客户端消息后调用的方法
     */
    void onMessage(String message, String username);

    void onError(String username);

    default void sendMessage(String message, String username){
        Session session = WebSocketServer.clients.get(username);
        if (session == null) {
            return;
        }
        synchronized (session) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    default void sendGroupMessage(String message, Set<String> set){
        for(String s:set){
            sendMessage(message,s);
        }
    }

    default void sendAllMessage(String message){
        for(String s:WebSocketServer.clients.keySet()){
            sendMessage(message,s);
        }
    }
}
