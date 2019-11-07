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

import com.atguigu.gmall.sms.entity.SkuBoundsCopyEntity;
import com.atguigu.gmall.sms.service.SkuBoundsCopyService;




/**
 * 商品sku积分设置
 *
 * @author gesanqiang
 * @email san@atguigu.com
 * @date 2019-10-30 20:21:03
 */
@Api(tags = "商品sku积分设置 管理")
@RestController
@RequestMapping("sms/skuboundscopy")
public class SkuBoundsCopyController {
    @Autowired
    private SkuBoundsCopyService skuBoundsCopyService;

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sms:skuboundscopy:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = skuBoundsCopyService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sms:skuboundscopy:info')")
    public Resp<SkuBoundsCopyEntity> info(@PathVariable("id") Long id){
		SkuBoundsCopyEntity skuBoundsCopy = skuBoundsCopyService.getById(id);

        return Resp.ok(skuBoundsCopy);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sms:skuboundscopy:save')")
    public Resp<Object> save(@RequestBody SkuBoundsCopyEntity skuBoundsCopy){
		skuBoundsCopyService.save(skuBoundsCopy);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sms:skuboundscopy:update')")
    public Resp<Object> update(@RequestBody SkuBoundsCopyEntity skuBoundsCopy){
		skuBoundsCopyService.updateById(skuBoundsCopy);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sms:skuboundscopy:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids){
		skuBoundsCopyService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
