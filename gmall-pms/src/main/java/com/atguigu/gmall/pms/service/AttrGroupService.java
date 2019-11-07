package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.AttrGroupVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 属性分组
 *
 * @author sx
 * @email sx@atguigu.com
 * @date 2019-10-28 20:04:41
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageVo queryPage(QueryCondition params);

    //2.2.1.   查询三级分类的分组
    PageVo queryByCidPage(Long catId, QueryCondition queryCondition);

    //2.2.3.   查询组及组的规格参数  :维护关联按钮
    AttrGroupVO queryGroupWithAttr(Long gid);


    //2.2.6.   查询分类下的组及规格参数
    List<AttrGroupVO> queryGroupWithAttrsByCid(Long catId);
}

