package com.atguigu.gmall.wms.api;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface GmallWmsApi {
    /**
     * （5）根据skuId查询库存(和pmsInterface其他接口组合)
     * @param skuId
     * @return
     */
    @GetMapping("wms/waresku/{skuId}")
    public Resp<List<WareSkuEntity>> queryWareBySkuId(@PathVariable("skuId")Long skuId);
}
