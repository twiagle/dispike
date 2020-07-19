package com.twiagle.dispike.common.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Orders {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long  deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private LocalDateTime createDate;
    private LocalDateTime payDate;
}
