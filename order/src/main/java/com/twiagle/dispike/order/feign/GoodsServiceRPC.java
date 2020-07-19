package com.twiagle.dispike.order.feign;

import com.twiagle.dispike.common.vo.GoodsVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("product")
public interface GoodsServiceRPC {

    @ResponseBody
    @GetMapping("/goods/goodsVo/{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@PathVariable long goodsId);

    @ResponseBody
    @PutMapping("/goods/stock")
    public boolean reduceStock(@RequestBody GoodsVo goodsVo);

    @ResponseBody
    @PutMapping("/goods/resetAll")
    public void resetStock(@RequestBody List<GoodsVo> goodsList);

    @ResponseBody
    @GetMapping("/goods/goodsVo/listAll")
    public List<GoodsVo> listGoodsVo();
}
