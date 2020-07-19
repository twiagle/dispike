package com.twiagle.dispike.common.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpikeGoods {
    private Long id;//auto increment
    private Long goodsId;//Goods.id
    private Double spikePrice;
    private Integer stockCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
