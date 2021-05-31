package com.alsritter.controller;

import com.alsritter.entity.GameDeathInfoDTO;
import com.alsritter.entity.GameEndInfoDTO;
import com.alsritter.entity.GameMapInfoDTO;
import com.alsritter.entity.GameUserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@CrossOrigin
@RestController
public class TempController {
    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello this is game service");
    }

    /**
     * 通过 code 登陆
     *
     * @param code 用户 code
     * @return
     */
    @GetMapping("/login_code/{code}")
    public ResponseEntity<GameUserInfoDTO> loginCode(@PathVariable String code) {
        log.debug(code);
        return ResponseEntity.ok(new GameUserInfoDTO("alsritter", "this is token...", 12, 30, 100));
    }

    /**
     * 取得地图信息
     *
     * @return
     */
    @GetMapping("/get_map_infos")
    public ResponseEntity<List<GameMapInfoDTO>> getMapInfo() {
        String image = "https://images.alsritter.icu/images/2021/05/15/20210515204220.png";
        String image02 = "https://images.alsritter.icu/images/2021/05/16/20210516143220.png";
        String image03 = "https://i.loli.net/2021/05/17/Taq1XZDyudnG5RQ.png";

        List<GameMapInfoDTO> gameMapInfoDTOS = Arrays.<GameMapInfoDTO>asList(
                new GameMapInfoDTO("1", "alsritter", "这是一张简单的地图", image, 12, 2, 20, "v1.0", 5),
                new GameMapInfoDTO("2", "luotao", "这是一张测试地图01", image03, 12, 2, 20, "v1.0", 2),
                new GameMapInfoDTO("3", "xiaoai", "这是一张很难的测试地图", image, 12, 2, 20, "v1.0", 1),
                new GameMapInfoDTO("4", "zhangga", "lingshidiad", image02, 12, 2, 20, "v1.0", 1),
                new GameMapInfoDTO("5", "xiaowu", "dasdasdasdad", image03, 12, 2, 20, "v1.0", 1),
                new GameMapInfoDTO("6", "rotter", "这是一张地图", image02, 12, 2, 20, "v1.0", 3));

        return ResponseEntity.ok(gameMapInfoDTOS);
    }

    /**
     * 推送角色死亡信息,这个是受伤一次的信息
     *
     * @param info 角色死亡信息
     * @return
     */
    @PostMapping("/push_death_info")
    public ResponseEntity<String> pushDeathInfo(@RequestBody GameDeathInfoDTO info) {
        log.debug(info.toString());
        return ResponseEntity.ok("ok");
    }

    /**
     * 推送游戏结束的信息
     * 游戏结束包括：胜利、失败
     *
     * @param info 游戏结束的信息
     * @return
     */
    @PostMapping("/push_end_info")
    public ResponseEntity<String> pushEndInfo(@RequestBody GameEndInfoDTO info) {
        log.debug(info.toString());
        return ResponseEntity.ok("ok");
    }

    @Autowired
    private ApplicationContext ctx;

    /**
     * 通过地图编号来取得地图数据
     *
     * @param mapId
     * @return
     * @throws IOException
     */
    @GetMapping("/get_map/{mapId}")
    public ResponseEntity<String> getMap(@PathVariable String mapId) throws IOException {
        File file = ctx.getResource("classpath:testData.json").getFile();
        StringBuilder content = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        }

        log.debug(mapId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(content.toString());
    }

    /**
     * 记录玩家游玩了地图
     *
     * @param mapId
     * @return
     */
    @GetMapping("/play_map/{mapId}")
    ResponseEntity<String> playMapById(@PathVariable String mapId) {
        log.debug("玩家游玩了" + mapId);
        return ResponseEntity.ok("ok");
    }
}
