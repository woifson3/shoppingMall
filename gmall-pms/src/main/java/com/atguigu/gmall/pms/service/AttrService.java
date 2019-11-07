package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 商品属性
 *
 * @author sx
 * @email sx@atguigu.com
 * @date 2019-10-28 20:04:41
 */
public interface AttrService extends IService<AttrEntity> {

    PageVo queryPage(QueryCondition params);

    /**
     *2.3.   规格属性查询
     */
    PageVo queryAttrByCid(QueryCondition queryCondition, Long cid, Integer type);

    //2.3.5.   保存规格参数
    void saveAttrVoAndRelation(AttrVo attrVo);
}

