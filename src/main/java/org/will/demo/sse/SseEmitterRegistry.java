package org.will.demo.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterRegistry {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    // 每个连接一把发送锁：SseEmitter 不允许并发 send，否则抛 IllegalStateException
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    /** 注册新连接，clientId 可用 userId / serialNo / UUID */
    public SseEmitter register(String clientId) {
        // 0L = 永不超时（配合心跳保活）；不想永续可设如 5 * 60 * 1000L
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(clientId, emitter);
        locks.put(clientId, new Object());

        emitter.onCompletion(() -> remove(clientId));
        emitter.onTimeout(() -> remove(clientId));
        emitter.onError(e -> remove(clientId));
        return emitter;
    }

    /** 向指定客户端推送，eventName 为 null 时发默认 message 事件 */
    public boolean send(String clientId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(clientId);
        Object lock = locks.get(clientId);
        if (emitter == null || lock == null) {
            return false;
        }
        synchronized (lock) {
            try {
                SseEmitter.SseEventBuilder builder = SseEmitter.event()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .data(data);
                if (eventName != null && !eventName.isEmpty()) {
                    builder.name(eventName);
                }
                emitter.send(builder);
                return true;
            } catch (Exception e) {
                remove(clientId); // 发送失败视为连接已断
                return false;
            }
        }
    }

    /** 广播给所有在线客户端 */
    public int sendAll(String eventName, Object data) {
        int count = 0;
        for (String id : emitters.keySet()) {
            if (send(id, eventName, data)) {
                count++;
            }
        }
        return count;
    }

    /** 心跳：向所有连接发注释行，保活但不触发前端事件 */
    public void heartbeatAll() {
        for (String id : emitters.keySet()) {
            SseEmitter emitter = emitters.get(id);
            Object lock = locks.get(id);
            if (emitter == null || lock == null) {
                continue;
            }
            synchronized (lock) {
                try {
                    emitter.send(SseEmitter.event().comment("heartbeat"));
                } catch (Exception e) {
                    remove(id);
                }
            }
        }
    }

    public void remove(String clientId) {
        emitters.remove(clientId);
        locks.remove(clientId);
    }

    public int onlineCount() {
        return emitters.size();
    }
}
