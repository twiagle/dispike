package com.twiagle.dispike.product.service;


import com.twiagle.dispike.common.entities.SpikeGoods;
import com.twiagle.dispike.common.vo.GoodsVo;
import com.twiagle.dispike.product.dao.GoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goodsVo) {
        SpikeGoods g = new SpikeGoods();
        g.setGoodsId(goodsVo.getId());
        int val = goodsDao.reduceStock(g);
        return val > 0;
    }

    public void resetStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList ) {
            SpikeGoods g = new SpikeGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDao.resetStock(g);
        }
    }
}
