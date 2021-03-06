package com.chen.service.impl;

import com.chen.exception.SellException;
import com.chen.service.RedisLock;
import com.chen.util.KeyUtil;
import io.lettuce.core.dynamic.domain.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SecKillService {

    private static final int TIMEOUT = 10 * 1000;//超时时间 10s

    @Autowired
    private RedisLock redisLock;

    static Map<String,Integer> products;
    static Map<String,Integer> stock;
    static Map<String,String> orders;
    static {
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("123456",100000);
        stock.put("123456",100000);
    }

    private String queryMap(String productId){
        return "国庆活动，皮蛋粥特价，限量份"
                + products.get(productId)
                +" 还剩" + stock.get(productId) + " 份"
                +" 该商品成功下单用户数目："
                + orders.size()+" 人";
    }

    public String querySecKillProductInfo(String productId){
        return  this.queryMap(productId);
    }

    public void orderProductMockDiffUser(String productId){
        //加锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if (!redisLock.lock(productId, String.valueOf(time))){
            throw new SellException(101,"哎呦喂，人也太多了，换个姿势在试试~~");
        }

        int stockNum = stock.get(productId);
        if (stockNum == 0){
            throw new SellException(100,"活动结束");
        }else {
            orders.put(KeyUtil.genUniqueKey(),productId);
            stockNum = stockNum-1;
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            stock.put(productId,stockNum);
        }

        //解锁
        redisLock.unlock(productId,String.valueOf(time));
    }
}
