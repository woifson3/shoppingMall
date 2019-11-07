package com.atguigu.gmall.pms.controller;

import java.util.Arrays;
import java.util.Map;


import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.vo.AttrVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.service.AttrService;




/**
 * 商品属性
 *
 * @author sx
 * @email sx@atguigu.com
 * @date 2019-10-28 20:04:41
 */
@Api(tags = "商品属性 管理")
@RestController
@RequestMapping("pms/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 2.3.5.   保存规格参数
     * 商品管理--》属性维护-->添加属性
     * 需要添加的是attr和relation中间表的信息
     */
    @ApiOperation("添加属性")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:attr:save')")
    public Resp<Object> save(@RequestBody AttrVo attrVo){//attrVO就是我们扩展字段之后的VO

        attrService.saveAttrVoAndRelation(attrVo);

        return Resp.ok("保存成功");
    }


    /**
     * 2.3.   规格属性查询
     *
     *三级分类下查询规格，属性参数的  方法:属性分组，基本属性，销售属性
     * @param type  ：看RequestURL中？后面的参数，就是说打开这个列表，需要传递哪些参数就在URL？后面有提示  。Required=false代表要传的这个参数可以是空
     * @param cid
     * @param condition  ：自己封装的页面查询信息
     * @return
     */
    @ApiOperation(" 规格属性查询")
    @GetMapping
    public Resp<PageVo> queryAttrByCid(@RequestParam(value = "type",required = false)Integer type,@RequestParam(value = "cid",required = false) Long cid,QueryCondition condition){
        PageVo pageVo=attrService.queryAttrByCid(condition,cid,type);
        return Resp.ok(pageVo);
    }


    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:attr:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = attrService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{attrId}")
    @PreAuthorize("hasAuthority('pms:attr:info')")
    public Resp<AttrEntity> info(@PathVariable("attrId") Long attrId){
		AttrEntity attr = attrService.getById(attrId);

        return Resp.ok(attr);
    }


    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:attr:update')")
    public Resp<Object> update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:attr:delete')")
    public Resp<Object> delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return Resp.ok(null);
    }

}
