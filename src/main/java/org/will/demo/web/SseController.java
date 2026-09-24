package org.will.demo.web;

import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.will.demo.sse.SseEmitterRegistry;

import java.util.Map;

@RestController
@RequestMapping("/sse")
public class SseController {

    @Resource
    private SseEmitterRegistry registry;

    /** 客户端订阅（EventSource 只支持 GET，认证走 query 参数或 Cookie） */
    @GetMapping(value = "/subscribe/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String clientId) {
        return registry.register(clientId);
    }

    /** 测试：向指定客户端推一条消息 */
    @PostMapping("/push/{clientId}")
    public ResponseEntity<String> push(@PathVariable String clientId, @RequestBody Map<String, Object> data) {
        boolean ok = registry.send(clientId, "message", data);
        return ok ? ResponseEntity.ok("sent") : ResponseEntity.status(404).body("客户端未连接");
    }

    /** 测试：广播 */
    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok("已广播给 " + registry.sendAll("message", data) + " 个客户端");
    }
}
