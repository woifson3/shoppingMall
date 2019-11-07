package com.atguigu.gmall.sms.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.sms.dao.SkuBoundsCopy1Dao;
import com.atguigu.gmall.sms.entity.SkuBoundsCopy1Entity;
import com.atguigu.gmall.sms.service.SkuBoundsCopy1Service;


@Service("skuBoundsCopy1Service")
public class SkuBoundsCopy1ServiceImpl extends ServiceImpl<SkuBoundsCopy1Dao, SkuBoundsCopy1Entity> implements SkuBoundsCopy1Service {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuBoundsCopy1Entity> page = this.page(
                new Query<SkuBoundsCopy1Entity>().getPage(params),
                new QueryWrapper<SkuBoundsCopy1Entity>()
        );

        return new PageVo(page);
    }

}