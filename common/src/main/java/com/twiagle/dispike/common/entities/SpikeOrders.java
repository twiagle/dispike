package com.twiagle.dispike.common.entities;

import lombok.Data;

@Data
public class SpikeOrders {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
