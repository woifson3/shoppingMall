package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.vo.ProductAttrValueVO;
import com.atguigu.gmall.pms.vo.SpuAttributeValueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.ProductAttrValueDao;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.service.ProductAttrValueService;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageVo(page);
    }

    @Autowired
    private ProductAttrValueDao attrValueDao;
    /**
     * 编写远程接口(1)
     *
     * @param spuId
     * @return
     */
    @Override
    public List<SpuAttributeValueVO> querySearchAttrValue(Long spuId) {
        List<ProductAttrValueEntity> productAttrValueEntities = attrValueDao.querySearchAttrValue(spuId);
/**
 * ======================
 * ****从数据库中拿到 页面中需要的List<VO>中一个个VO需要的AttrID，attrName,attrValue三个值。然后从Entity表数据转到VO表中，传到页面中显示***
 * ======================
 */
        return productAttrValueEntities.stream().map(productAttrValueEntitiy-> {
            SpuAttributeValueVO spuAttributeValueVO = new SpuAttributeValueVO();

            spuAttributeValueVO.setProductAttributeId(productAttrValueEntitiy.getAttrId());
            spuAttributeValueVO.setName(productAttrValueEntitiy.getAttrName());
            spuAttributeValueVO.setValue(productAttrValueEntitiy.getAttrValue());

            return spuAttributeValueVO;//把装好数据的VO对象给展示到页面
        }).collect(Collectors.toList()); //新的数据以集合的方式装起来

    }

}