package com.daniel.seckill.controller;

import com.daniel.seckill.model.User;
import com.daniel.seckill.service.GoodsService;
import com.daniel.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 商品相关的Controller
 *
 * @author DanielLin07
 * @date 2019/1/19 16:27
 */
@Controller
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("toList")
    public String toList(Model model, User user) {
        model.addAttribute("user", user);
        // 查询商品列表页
        List<GoodsVO> goodsVOList = goodsService.queryListGoodsVO();
        model.addAttribute("goodsVOList", goodsVOList);
        return "goodsList";
    }

    @RequestMapping("toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable("goodsId") Long goodsId) {
        model.addAttribute("user", user);

        // 根据商品Id查询商品详情
        if (goodsId == null) {
            return "goodsList";
        }
        GoodsVO goodsVO = goodsService.queryGoodsVOById(goodsId);
        model.addAttribute("goodsVO", goodsVO);

        long startTime = goodsVO.getStartDate().getTime();
        long endTime = goodsVO.getEndDate().getTime();
        long currentTime = System.currentTimeMillis();

        // 记录秒杀状态以及倒计时
        long seckillStatus = 0;
        long remainSeconds = 0;

        // 秒杀还未开始，处于倒计时状态
        if (currentTime < startTime) {
            remainSeconds = (startTime - currentTime) / 1000;
        // 秒杀已经结束
        } else if (currentTime > endTime) {
            seckillStatus = 2;
        // 秒杀正在进行中
        } else {
            seckillStatus = 1;
        }

        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goodsDetail";
    }
}
