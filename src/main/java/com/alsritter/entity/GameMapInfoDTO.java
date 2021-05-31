package com.alsritter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地图信息，这里只包含地图的介绍信息，
 * 地图数据存在其它地方
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameMapInfoDTO implements Serializable {
    @JsonProperty("id")
    private String id;              // 地图编号
    @JsonProperty("author")
    private String author;          // 作者名称
    @JsonProperty("introduction")
    private String introduction;    // 地图介绍
    @JsonProperty("cover_path")
    private String coverPath;       // 地图封面
    @JsonProperty("down_count")
    private int downCount;          // 下载次数
    @JsonProperty("pass_count")
    private int passCount;           // 通过次数
    @JsonProperty("sum_count")
    private int sumCount;           // 游玩总次数
    @JsonProperty("version")
    private String version;         // 地图版本
    @JsonProperty("grade")
    private int grade;              // 地图评分
}
