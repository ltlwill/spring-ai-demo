package org.will.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.will.demo.sse.SseEmitterRegistry;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 30 秒一次注释行，防止 SseEmitter 空闲超时和 nginx 等代理断掉空闲连接
 */
@Component
public class SseHeartbeatConfig {

    private static final ScheduledExecutorService HEARTBEAT =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "sse-heartbeat");
                t.setDaemon(true);
                return t;
            });

    private final SseEmitterRegistry registry;

    public SseHeartbeatConfig(SseEmitterRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void start() {
        HEARTBEAT.scheduleAtFixedRate(registry::heartbeatAll, 30, 30, TimeUnit.SECONDS);
    }
}
