package com.twiagle.dispike.spike.controller;


import com.twiagle.dispike.common.entities.SpikeUser;
import com.twiagle.dispike.common.result.CodeMsg;
import com.twiagle.dispike.common.result.Result;
import com.twiagle.dispike.common.vo.GoodsVo;
import com.twiagle.dispike.spike.feign.GoodsServiceRPC;
import com.twiagle.dispike.spike.feign.OrderServiceRPC;
import com.twiagle.dispike.spike.rabbitmq.MQSender;
import com.twiagle.dispike.spike.rabbitmq.SpikeMessage;
import com.twiagle.dispike.spike.redis.GoodsPrefix;
import com.twiagle.dispike.spike.redis.RedisService;
import com.twiagle.dispike.spike.service.SpikeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/spike")
public class SpikeController implements InitializingBean {
    @Autowired
    GoodsServiceRPC goodsServiceRPC;
    @Autowired
    SpikeService spikeService;
    @Autowired
    OrderServiceRPC orderServiceRPC;

    @Autowired
    MQSender mqSender;
    @Autowired
    RedisService redisService;

    private Map<Long, Boolean> soldOutFlag = new HashMap<>();//only for this Controller

    @RequestMapping("/do_spike")
    @ResponseBody
    Result<Integer> spike(SpikeUser spikeUser, @RequestParam("goodsId") long goodsId) {
        if (spikeUser == null) return Result.error(CodeMsg.SESSION_ERROR);

        //redis 预减库存,这里有个问题：没考虑取消订单，所以不存在redis增加操作，减为负一定卖光。这里不控制并发，数据库保证
        if(soldOutFlag.get(goodsId)) return Result.error(CodeMsg.MIAO_SHA_OVER);
        long stock = redisService.decr(GoodsPrefix.getSpikeGoodsStock, "" + goodsId);
        if(stock<0){
            soldOutFlag.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        SpikeMessage spikeMessage = new SpikeMessage(spikeUser.getId(), goodsId);
        mqSender.sendSpikeMessage(spikeMessage);
        return Result.success(0);//排队中
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method= RequestMethod.GET)
    @ResponseBody
    public Result<Long> spikeResult(SpikeUser user, @RequestParam("goodsId")long goodsId) {
        if(user == null) return Result.error(CodeMsg.SESSION_ERROR);

        long result = spikeService.getSpikeResult(user.getId(), goodsId);
        return Result.success(result);
    }

//    @RequestMapping(value="/reset", method= RequestMethod.GET)
//    @ResponseBody
//    public Result<Boolean> reset(Model model) {
//        List<GoodsVo> goodsList = goodsService.listGoodsVo();
//        for(GoodsVo goods : goodsList) {
//            goods.setStockCount(10);//spike count = 10
//            redisService.set(GoodsPrefix.getSpikeGoodsStock, ""+goods.getId(), 10);
//            soldOutFlag.put(goods.getId(), false);
//        }
//        redisService.delete(SpikePrefix.isGoodsSoldOut);
//        redisService.delete(OrderPrefix.getSpikeOrderByUidGid);
//        spikeService.reset(goodsList);
//        return Result.success(true);
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsServiceRPC.listGoodsVo();
        if(goodsList == null) {
            return;
        }
        for(GoodsVo goods : goodsList) {
            redisService.set(GoodsPrefix.getSpikeGoodsStock, ""+goods.getId(), goods.getStockCount());
            soldOutFlag.put(goods.getId(), false);
        }
    }
}
