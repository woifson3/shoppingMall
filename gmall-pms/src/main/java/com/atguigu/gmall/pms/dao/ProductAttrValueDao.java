package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.vo.ProductAttrValueVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * spu属性值
 * 
 * @author sx
 * @email sx@atguigu.com
 * @date 2019-10-28 20:04:41
 */
@Mapper
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {

    /**
     * querySearchAttrValue
     * 这里返回的是Entiy啊，只有Entity才可以和DB交互
     *
     * @param spuId
     * @return
     */
    List<ProductAttrValueEntity> querySearchAttrValue(Long spuId);
}
