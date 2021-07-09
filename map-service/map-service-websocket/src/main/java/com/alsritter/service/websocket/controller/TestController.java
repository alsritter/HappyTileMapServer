package com.alsritter.service.websocket.controller;

import com.alsritter.service.websocket.config.RedisHashManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;

/**
 * @author alsritter
 * @version 1.0
 **/
@AllArgsConstructor
@RestController
public class TestController {
    private final RedisHashManager<Object> redisHashManager;

    // 测试用来修改地图的操作
    private final String MAP_FLAG = "MAP_FLAG";

    @PostMapping("/test")
    public ResponseEntity<Integer> test(@RequestBody Map<String, String> body) {
        // 假设这个地图的 id 是下面这个
        String mapKey = "1000001";
        Integer value = new Random().nextInt(100);
        // 假设传入的参数是：坐标、这个位置的值
        redisHashManager.put(MAP_FLAG + mapKey, body.get("cc"), value);
        // redisTemplate.
        return ResponseEntity.ok().body(value);
    }
}
