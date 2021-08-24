package com.alsritter.starter.websocket.model;

import lombok.Data;

/**
 * 前端传进来的地图数据
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
public class MapDataVo {
    /**
     * coordinate 坐标 0-2 这种形式表示 x-y
     */
    private String cc;
    private String layer;
    private String value;
    /**
     * 时间戳，用来标识这个数据发过来的时间
     */
    private Long time;
}
