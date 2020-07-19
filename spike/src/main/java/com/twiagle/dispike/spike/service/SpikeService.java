package com.twiagle.dispike.spike.service;

import com.twiagle.dispike.common.entities.SpikeOrders;
import com.twiagle.dispike.common.entities.SpikeUser;
import com.twiagle.dispike.common.vo.GoodsVo;
import com.twiagle.dispike.spike.feign.GoodsServiceRPC;
import com.twiagle.dispike.spike.feign.OrderServiceRPC;
import com.twiagle.dispike.spike.redis.RedisService;
import com.twiagle.dispike.spike.redis.SpikePrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SpikeService {
    @Autowired
    GoodsServiceRPC goodsServiceRPC;
    @Autowired
    OrderServiceRPC orderServiceRPC;
    @Autowired
    RedisService redisService;


    //两个并发用户可能对stock=1都走到这里，或者50用户个对stock=2走到这里，借助DB where stock > 0
    //同一个用户两个并发请求，而且stock>1. 请求都通过了库存、重复订单的校验，那么将发起两个事务，解决方法：建立spike_orders的userId和goodsId唯一组合索引，这样第二个事务就会回滚
    @Transactional
    public void spike(SpikeUser spikeUser, GoodsVo goodsVo) {

        boolean success = goodsServiceRPC.reduceStock(goodsVo);
        if(success){
            orderServiceRPC.createOrder(spikeUser, goodsVo.getId());
        }else{//distinguish sold-out or still processing in message queue
            setGoodsSoldOut(goodsVo.getId());
        }
    }

    public long getSpikeResult(long userId, long goodsId) {
        //对于同一个用户重复秒杀下单，将在spike函数里制止，但没有返回提示，前端还是会查询，这里会根据UserIDGoodsID查询到之前的订单
        SpikeOrders order = orderServiceRPC.getSpikeOrderByUserIDGoodsID(userId, goodsId);
        if(order != null) {//spike success
            return order.getOrderId();
        }else if(isGoodsSoldOut(goodsId)){//sold-out
            return -1;
        }else return 0;//still processing in message queue
    }

    public void setGoodsSoldOut(long goodsId) {
        redisService.set(SpikePrefix.isGoodsSoldOut, ""+goodsId, true);
    }

    private boolean isGoodsSoldOut(long goodsId) {
        return redisService.exists(SpikePrefix.isGoodsSoldOut, ""+goodsId);
    }

    public void reset(List<GoodsVo> goodsList) {
        goodsServiceRPC.resetStock(goodsList);
        orderServiceRPC.deleteOrders();
    }
}
