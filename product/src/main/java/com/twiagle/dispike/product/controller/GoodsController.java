package com.twiagle.dispike.product.controller;


import com.twiagle.dispike.common.entities.SpikeUser;
import com.twiagle.dispike.common.result.Result;
import com.twiagle.dispike.common.vo.GoodsDetailVo;
import com.twiagle.dispike.common.vo.GoodsVo;
import com.twiagle.dispike.product.redis.GoodsPrefix;
import com.twiagle.dispike.product.redis.RedisService;
import com.twiagle.dispike.product.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneOffset;
import java.util.List;

@Controller
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * redis cache html
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/goods/to_list")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {

        String html = redisService.get(GoodsPrefix.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) return html;

        List<GoodsVo> goodsList = goodsService.listGoodsVo();

        model.addAttribute("goodsList", goodsList);
        IContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);

        if (!StringUtils.isEmpty(html)) redisService.set(GoodsPrefix.getGoodsList, "", html);
        return html;
    }

    /**
     * browser store static htm, ajax get data
     *
     * @param model
     * @param spikeUser
     * @param goodsId
     * @return
     */
    @RequestMapping("/goods/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(Model model, SpikeUser spikeUser, @PathVariable("goodsId") long goodsId) {

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goodsVo.getStartDate().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long endAt = goodsVo.getEndDate().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long now = System.currentTimeMillis();
        int spikeStatus = 0;
        int remainSeconds = 0;

        if (now < startAt) {
            spikeStatus = 0;// not begin
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            spikeStatus = 2;// end
            remainSeconds = -1;
        } else {
            spikeStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = GoodsDetailVo.builder().goodsVo(goodsVo).spikeUser(spikeUser).spikeStatus(spikeStatus).remainSeconds(remainSeconds).build();

        return Result.success(goodsDetailVo);
    }

    /**
     * RPC 接口
     */


    @ResponseBody
    @GetMapping("/goods/goodsVo/{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@PathVariable long goodsId){
        return goodsService.getGoodsVoByGoodsId(goodsId);
    }

    @ResponseBody
    @PutMapping("/goods/stock")
    public boolean reduceStock(@RequestBody GoodsVo goodsVo){
        return goodsService.reduceStock(goodsVo);
    }

    @ResponseBody
    @PutMapping("/goods/resetAll")
    public void resetStock(@RequestBody List<GoodsVo> goodsList){
        goodsService.resetStock(goodsList);
    }

    @ResponseBody
    @GetMapping("/goods/goodsVo/listAll")
    public List<GoodsVo> listGoodsVo(){
        return goodsService.listGoodsVo();
    }

}
