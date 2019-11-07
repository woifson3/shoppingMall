package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.SpuInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * spu信息
 *
 * @author sx
 * @email sx@atguigu.com
 * @date 2019-10-28 20:04:41
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);
    /**
     * 2.3.1.   检索商品
     * @param catId
     * @param condition
     * @return
     */
    PageVo querySpuInfoByKeyPage(Long catId, QueryCondition condition);

    void bigSave(SpuInfoVO spuInfoVO);


}

