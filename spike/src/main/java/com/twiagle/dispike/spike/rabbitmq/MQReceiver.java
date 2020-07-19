package com.twiagle.dispike.spike.rabbitmq;


import com.twiagle.dispike.common.entities.SpikeOrders;
import com.twiagle.dispike.common.entities.SpikeUser;
import com.twiagle.dispike.common.vo.GoodsVo;
import com.twiagle.dispike.spike.feign.CustomerServiceRPC;
import com.twiagle.dispike.spike.feign.GoodsServiceRPC;
import com.twiagle.dispike.spike.feign.OrderServiceRPC;
import com.twiagle.dispike.spike.redis.RedisService;
import com.twiagle.dispike.spike.service.SpikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    GoodsServiceRPC goodsServiceRPC;
    @Autowired
    OrderServiceRPC orderServiceRPC;
    @Autowired
    SpikeService spikeService;
    @Autowired
    CustomerServiceRPC customerServiceRPC;
//默认单线程，可以通过指定SimpleRabbitListenerContainerFactory工厂 containerFactory = "customContainerFactory")来设置并发线程数
    @RabbitListener(queues = MQConfig.SPIKE_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);

        SpikeMessage spikeMessage = RedisService.stringToBean(message, SpikeMessage.class);
        long userId = spikeMessage.getUserId();
        long goodsId = spikeMessage.getGoodsId();
        SpikeUser user = customerServiceRPC.getById(userId);
        GoodsVo goods = goodsServiceRPC.getGoodsVoByGoodsId(goodsId);

        if(goods.getStockCount()<=0) {
            spikeService.setGoodsSoldOut(goodsId);
            return;
        }
        SpikeOrders spikeOrders = orderServiceRPC.getSpikeOrderByUserIDGoodsID(userId, goodsId);
        if(spikeOrders != null) return;

        spikeService.spike(user, goods);
        log.info("spike over");
    }
}
