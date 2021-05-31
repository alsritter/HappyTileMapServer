package com.alsritter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色死亡时提交的记录
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDeathInfoDTO implements Serializable {
    // 死亡位置
    @JsonProperty("x")
    private float x;
    // 死亡位置
    @JsonProperty("y")
    private float y;
}
