package com.twiagle.dispike.spike.feign;

import com.twiagle.dispike.common.entities.Orders;
import com.twiagle.dispike.common.entities.SpikeOrders;
import com.twiagle.dispike.common.entities.SpikeUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("order")
public interface OrderServiceRPC {

    @ResponseBody
    @GetMapping("/order/spikeOrder")
    public SpikeOrders getSpikeOrderByUserIDGoodsID(@RequestParam("userId") long userId, @RequestParam("goodsId") long goodsId);

    @ResponseBody
    @GetMapping("/order/{orderId}")
    public Orders getOrderById(@PathVariable long orderId);

    @ResponseBody
    @PostMapping("/order")
    public Orders createOrder(@RequestBody SpikeUser user, @RequestParam long goodsId);


    @ResponseBody
    @DeleteMapping("/order/deleteAll")
    public void deleteOrders();
}
