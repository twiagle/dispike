package com.twiagle.dispike.common.vo;

import com.twiagle.dispike.common.entities.SpikeUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoodsDetailVo {
    private int spikeStatus;
    private int remainSeconds;
    private GoodsVo goodsVo;
    private SpikeUser spikeUser;
}
