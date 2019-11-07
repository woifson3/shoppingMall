package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrDao attrDao;
    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageVo(page);
    }

    /**
     * 2.3.   规格属性查询
     *
     * 三级分类下查询规格，属性参数的  方法
     */
    @Override
    public PageVo queryAttrByCid(QueryCondition condition,Long cid,Integer type) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        if (type != null) {
            wrapper.eq("attr_type",type);
        }
        if (cid !=null) {
            wrapper.eq("catelog_id",cid);
        }
//分页用//这些方法就是用来进行浏览器和后端分页信息参数转化传递的
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(condition)//**利用页面点击的得到的condition，进行数据的查询，查询的sql是wrapper提供的，具体的sql是上面两个if中得到的**
                ,wrapper
        );

        return new PageVo(page);
    }


    /**
     * 2.3.5.   保存规格参数
     * 商品管理--》属性维护-->添加属性
     */
    @Transactional
    @Override
    public void saveAttrVoAndRelation(AttrVo attrVo) {//AttrVo继承了AttrEntity，添加了Long attrGroupId属性值
        //1.把 attr属性表的参数插入到数据库
        this.save(attrVo);
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        //这个是relation中间表表的所有三列参数
        relationEntity.setAttrId(attrVo.getAttrId());
        relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        relationEntity.setAttrSort(0);
        //2.把 中间表的信息 插入到数据库
         relationDao.insert(relationEntity);
    }
}