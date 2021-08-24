package com.alsritter.starter.websocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 发送消息的类
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/{sid}")
public class WebSocketServer {
    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static final AtomicInteger onlineCount = new AtomicInteger();
    // concurrent 包的线程安全 Set，用来存放每个客户端对应的 WebSocket  对象。
    private static final ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    // 连接的用户 id
    private String sid = "";
    // 当前修改的 mapId
    private String mapId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketMap.put(sid, this);     // 加入 set 中
        addOnlineCount();                // 在线数加 1
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());

        this.sid = sid;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // 收到心跳检测请求
        if ("0x9".equals(message)) {
            WebSocketServer item = webSocketMap.get(sid);
            try {
                item.sendMessage("0xA");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("收到来自窗口" + sid + "的信息:" + message);
        // do something...
    }

    @OnError
    public void onError(Session session, Throwable error) {
        onClose();
        log.info("非正常关闭,发生错误!====>" + error.toString() + "当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(sid);   // 从 set 中删除
        subOnlineCount();           // 在线数减 1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static int getOnlineCount() {
        return onlineCount.get();
    }

    public static void addOnlineCount() {
        WebSocketServer.onlineCount.addAndGet(1);
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount.addAndGet(-1);
    }
}
