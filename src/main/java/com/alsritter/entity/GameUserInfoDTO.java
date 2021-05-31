package com.alsritter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 玩家信息
 * @author alsritter
 * @version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameUserInfoDTO implements Serializable {
    @JsonProperty("username")
    private String username;
    @JsonProperty("token")
    private String token;
    @JsonProperty("win_count")
    private int winCount; // 用户胜利次数
    @JsonProperty("sum_count")
    private int sumCount; // 用户总场次
    @JsonProperty("death_count")
    private int deathCount; // 死亡次数
}
