package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.vo.AttrGroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.AttrGroupDao;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import org.springframework.util.CollectionUtils;

import javax.management.relation.Relation;


@Service("attrGroupService")
//AttrGroupServiceImpl继承ServiceImpl并实现AttrGroupService，ServiceImpl实现了IService，所以this.page是调取的父类封装实现的方法，做分页
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {


    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrDao attrDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(        //page方法里传两个参数，第一个参数为IPage<T> page，Query对象调取getPage(params)可以获取Page对象
                new Query<AttrGroupEntity>().getPage(params),//分页条件
                new QueryWrapper<AttrGroupEntity>()         //查询条件
        );

        return new PageVo(page);
    }

    /**
     * 2.2.1.   查询三级分类的分组
     *
     * @param catId
     * @param condition
     * @return
     */
    @Override
    public PageVo queryByCidPage(Long catId, QueryCondition condition) {

        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();//查询条件
        //判断cateId是否为空,catelog_id是列名，catId是查询条件
        if (catId != null) {
            wrapper.eq("catelog_id", catId);
        }

        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(condition),
                wrapper
        );
        return new PageVo(page);//这个分页对象是mybatis-plus的page，不一定是前端需要的分页对象（Bean-PageVo）。所以需要封装成前端需要的page
    }

    /**
     * 2.2.3.   查询组及组的规格参数
     *
     * @param gid
     * @return
     */
    @Override
    public AttrGroupVO queryGroupWithAttr(Long gid) {
        AttrGroupVO groupVO = new AttrGroupVO();//创建一个AttrGroupVO容器，用来装三个表的数据
        //1.查询group分组
        AttrGroupEntity groupEntity = getById(gid);
        BeanUtils.copyProperties(groupEntity, groupVO);//把groupEntity的值传给groupVO

        //2.利用group_id查询relation表  <>泛型，就是我们需要查询出什么，就在此种填什么
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", gid));
        if (relationEntities == null) {
            return groupVO;
        }
        groupVO.setRelations(relationEntities);//如果relation表查询出来的不是空的，就把之前的值加到relation中间表

        //3.利用reation的attrId查询attr表
        List<Long> attrIds = relationEntities.stream().map(relation -> relation.getAttrId()).collect(Collectors.toList());//把原本map中的元素relation通过relation.getAttrId()方式转化成collect格式
        List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);//利用Dao层查出attr表中的参数
        groupVO.setAttrEntities(attrEntities);//把参数设置到group中
        return groupVO;  //查询出 查询组及组的规格参数内容的容器groupVO
    }


    /**
     * 2.2.6.   查询分类下的组及规格参数
     *
     * 前端需要响应的数据涉及到的表有pms_group和attr
     *
     * @param catId
     * @return
     */
    @Override
    public List<AttrGroupVO> queryGroupWithAttrsByCid(Long catId) {
       /*  //1.先查出分类下的所有group组
        List<AttrGroupEntity> groupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));

        //2.再查出group以及所有attr信息
        *//**
         * attrGroupEntity :groupEntities中的一个个元素
         * attrGroupEntity.getAttrGroupId()：通过attrGroupEntity作为条件得到的新的元素
         * queryGroupWithAttr(attrGroupEntity.getAttrGroupId(：新的数据，就是group组以及attr元素
         *//*
        return groupEntities.stream().map(attrGroupEntity ->{
            return this.queryGroupWithAttr(attrGroupEntity.getAttrGroupId());
        }).collect(Collectors.toList());*/

        // 根据分类查询分类下的所有组
        List<AttrGroupEntity> groupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));

        // 查询每个组下的所有规格参数
        return groupEntities.stream().map(attrGroupEntity -> this.queryGroupWithAttr(attrGroupEntity.getAttrGroupId())).collect(Collectors.toList());
    }


}