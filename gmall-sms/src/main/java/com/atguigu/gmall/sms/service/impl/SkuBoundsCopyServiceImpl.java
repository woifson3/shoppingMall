package com.atguigu.gmall.sms.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.sms.dao.SkuBoundsCopyDao;
import com.atguigu.gmall.sms.entity.SkuBoundsCopyEntity;
import com.atguigu.gmall.sms.service.SkuBoundsCopyService;


@Service("skuBoundsCopyService")
public class SkuBoundsCopyServiceImpl extends ServiceImpl<SkuBoundsCopyDao, SkuBoundsCopyEntity> implements SkuBoundsCopyService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuBoundsCopyEntity> page = this.page(
                new Query<SkuBoundsCopyEntity>().getPage(params),
                new QueryWrapper<SkuBoundsCopyEntity>()
        );

        return new PageVo(page);
    }

}