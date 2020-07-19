package com.twiagle.dispike.spike.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@Builder
public class SpikeMessage {
    private long userId;
    private long goodsId;

//    public SpikeMessage(SpikeUser user, long goodsId) {
//        this.user = user;
//        this.goodsId = goodsId;
//    }
}
