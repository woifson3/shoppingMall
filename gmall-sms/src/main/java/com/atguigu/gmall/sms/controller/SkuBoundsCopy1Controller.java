package com.atguigu.gmall.sms.controller;

import java.util.Arrays;
import java.util.Map;


import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gmall.sms.entity.SkuBoundsCopy1Entity;
import com.atguigu.gmall.sms.service.SkuBoundsCopy1Service;




/**
 * 商品sku积分设置
 *
 * @author gesanqiang
 * @email san@atguigu.com
 * @date 2019-10-30 20:21:03
 */
@Api(tags = "商品sku积分设置 管理")
@RestController
@RequestMapping("sms/skuboundscopy1")
public class SkuBoundsCopy1Controller {
    @Autowired
    private SkuBoundsCopy1Service skuBoundsCopy1Service;

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sms:skuboundscopy1:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = skuBoundsCopy1Service.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sms:skuboundscopy1:info')")
    public Resp<SkuBoundsCopy1Entity> info(@PathVariable("id") Long id){
		SkuBoundsCopy1Entity skuBoundsCopy1 = skuBoundsCopy1Service.getById(id);

        return Resp.ok(skuBoundsCopy1);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sms:skuboundscopy1:save')")
    public Resp<Object> save(@RequestBody SkuBoundsCopy1Entity skuBoundsCopy1){
		skuBoundsCopy1Service.save(skuBoundsCopy1);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sms:skuboundscopy1:update')")
    public Resp<Object> update(@RequestBody SkuBoundsCopy1Entity skuBoundsCopy1){
		skuBoundsCopy1Service.updateById(skuBoundsCopy1);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sms:skuboundscopy1:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids){
		skuBoundsCopy1Service.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
