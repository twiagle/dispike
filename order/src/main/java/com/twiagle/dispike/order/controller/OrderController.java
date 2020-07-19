package com.twiagle.dispike.order.controller;


import com.twiagle.dispike.common.entities.Orders;
import com.twiagle.dispike.common.entities.SpikeOrders;
import com.twiagle.dispike.common.entities.SpikeUser;
import com.twiagle.dispike.common.result.CodeMsg;
import com.twiagle.dispike.common.result.Result;
import com.twiagle.dispike.common.vo.GoodsVo;
import com.twiagle.dispike.common.vo.OrderDetailVo;
import com.twiagle.dispike.order.feign.GoodsServiceRPC;
import com.twiagle.dispike.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsServiceRPC goodsServiceRPC;

    @RequestMapping("/order/detail/{orderId}")
    @ResponseBody
    public Result<OrderDetailVo> detail(SpikeUser spikeUser, @PathVariable("orderId") long orderId) {
        if(spikeUser == null) return Result.error(CodeMsg.SESSION_ERROR);

        Orders order = orderService.getOrderById(orderId);
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        GoodsVo goodsVo = goodsServiceRPC.getGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo orderDetailVo = OrderDetailVo.builder().goodsVo(goodsVo).order(order).build();

        return Result.success(orderDetailVo);
    }

    /**
     * RPC
     */
    @ResponseBody
    @GetMapping("/order/spikeOrder")
    public SpikeOrders getSpikeOrderByUserIDGoodsID(@RequestParam("userId") long userId, @RequestParam("goodsId") long goodsId){
        return orderService.getSpikeOrderByUserIDGoodsID(userId, goodsId);
    }

    @ResponseBody
    @GetMapping("/order/{orderId}")
    public Orders getOrderById(@PathVariable long orderId){
        return orderService.getOrderById(orderId);
    }

    @ResponseBody
    @PostMapping("/order")
    public Orders createOrder(@RequestBody SpikeUser user, @RequestParam long goodsId){

        GoodsVo goodsVo = goodsServiceRPC.getGoodsVoByGoodsId(goodsId);
        return orderService.createOrder(user, goodsVo);
    }

    @ResponseBody
    @DeleteMapping("/order/deleteAll")
    public void deleteOrders(){
        orderService.deleteOrders();
    }
}

