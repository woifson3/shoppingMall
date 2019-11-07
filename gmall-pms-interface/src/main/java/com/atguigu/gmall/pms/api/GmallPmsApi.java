package com.atguigu.gmall.pms.api;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.BrandEntity;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.gmall.pms.vo.SpuAttributeValueVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 这个是GoodsVO对应的接口，就是要获得网页呈现出来的全部信息。把前端装数据的VO和数据库装数据的Entity做数据传输
 *
 *
 * (1)分页查询spu
 * （2）根据spuid查询sku
 * (3)根据brandId查询brand
 * （4）根据categorygoryId查询分类
 * （5）根据skuId查询库存（在wsmInterface中）
 * （6）根据spuId查询检索属性
 */
public interface GmallPmsApi {
    /**
     *  （1 ）分页查询spu
     * @param queryCondition
     * @return
     */
    @PostMapping("pms/spuinfo/list")
    public Resp<List<SpuInfoEntity>> querySpuPage(@RequestBody QueryCondition queryCondition);
    /**
     * （2）根据spuid查询sku
     *
     * @param spuId
     * @return
     */
    @GetMapping("pms/skuinfo/{spuId}")
    public Resp<List<SkuInfoEntity>> querySkuBySpuId(@PathVariable("spuId")Long spuId);


    /**
     * (3)根据brandId查询brand
     *
     * ============使用fegin调用可以方法名不一样，只要mapping的映射地址一样就可以找到对应的调用方法========
     * @param brandId
     * @return
     */
    @GetMapping("pms/brand/info/{brandId}")
    public Resp<BrandEntity> querybrandById(@PathVariable("brandId") Long brandId);

    /**
     * （4）根据categorygoryId查询分类
     * @param catId
     * @return
     */
    @GetMapping("pms/category/info/{catId}")
    public Resp<CategoryEntity> queryCategoryById(@PathVariable("catId") Long catId);

    /**
     * （6）根据spuId查询检索属性
     * @param spuId
     * @return
     */
    @GetMapping("pms/productattrvalue/{spuId}")
    public Resp<List<SpuAttributeValueVO>> querySearchAttrValue(@PathVariable("spuId")Long spuId );


}
