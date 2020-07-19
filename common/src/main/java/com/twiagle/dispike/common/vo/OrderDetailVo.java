package com.twiagle.dispike.common.vo;

import com.twiagle.dispike.common.entities.Orders;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailVo {
    private GoodsVo goodsVo;
    private Orders order;
}
