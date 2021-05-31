package com.alsritter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 游戏结束的信息
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameEndInfoDTO implements Serializable {
    // 分数
    @JsonProperty("score")
    private int score;

    // 以秒为单位
    @JsonProperty("time")
    private float time;

    // 剩余 hp
    @JsonProperty("hp")
    private int hp;

    // 是否胜利
    @JsonProperty("win")
    private boolean win;
}
