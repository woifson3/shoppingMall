package com.atguigu.gmall.pms.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.vo.AttrGroupVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;




/**
 * 属性分组
 *
 * @author sx
 * @email sx@atguigu.com
 * @date 2019-10-28 20:04:41
 */
@Api(tags = "属性分组 管理")
@RestController
@RequestMapping("pms/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    /**
     *
     * 2.2.6.   查询分类下的组及规格参数
     *
     * 属性维护--》添加属性
     *
     * 对于泛型的选择就是。这个要查询的是谁，就填哪个对象。因为这个查的数据都在groupVO中有，就选择它。
     * 使用list是因为，还要不但得到group还要得到其下的所有attr属性的信息
     *
     * 查看前端给我的需求，是需要表中pms的group和attr的信息，并且请求地址：/pms/attrgroup/withattrs/cat/{catId}中需要一个{catId}  就通过过它去查找。catId是页面输入的
     * @param catId
     * @return
     */
    @GetMapping("withattrs/cat/{catId}")
    public Resp<List<AttrGroupVO>> queryGroupWithAttrsByCid(@PathVariable("catId")Long catId){//地址栏/后面的是占位符参数。？后面用RequestParam
        List<AttrGroupVO> groupVOS = this.attrGroupService.queryGroupWithAttrsByCid(catId);
        return Resp.ok(groupVOS);
    }


    /**
     * 2.2.3.   查询组及组的规格参数  :维护关联按钮
     *
     * @param gid :属性分组的id
     * @return
     */
    @ApiOperation("查询组及组的规格参数")
    @GetMapping("withattr/{gid}")
    public Resp<AttrGroupVO> queryGroupWithAttr(@PathVariable("gid") Long gid) {
        AttrGroupVO groupVO= attrGroupService.queryGroupWithAttr(gid);
        return Resp.ok(groupVO);
    }



    /**
     * 2.2.1.   查询三级分类的分组
     *
     * @param catId
     * @param condition
     * @return
     */
    //通过cid来查询  ，通过catId当参数传入
    // condition是自己封装的分页参数，catId是分页参数，catId通用分页参数
    @GetMapping("{catId}")
    public Resp<PageVo> queryByCidPage(@PathVariable("catId") Long catId, QueryCondition condition) {
        PageVo pageVo = attrGroupService.queryByCidPage(catId, condition);
        return Resp.ok(pageVo);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:attrgroup:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = attrGroupService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{attrGroupId}")
    @PreAuthorize("hasAuthority('pms:attrgroup:info')")
    public Resp<AttrGroupEntity> info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        return Resp.ok(attrGroup);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:attrgroup:save')")
    public Resp<Object> save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:attrgroup:update')")
    public Resp<Object> update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:attrgroup:delete')")
    public Resp<Object> delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return Resp.ok(null);
    }

}
