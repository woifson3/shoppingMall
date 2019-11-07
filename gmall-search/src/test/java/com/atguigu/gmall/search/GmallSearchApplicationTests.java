package com.atguigu.gmall.search;


import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.SpuAttributeValueVO;
import com.atguigu.gmall.search.feign.GmallPmsClient;
import com.atguigu.gmall.search.feign.GmallWmsClient;
import com.atguigu.gmall.search.vo.GoodsVO;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * ==================把数据库中的数据导入到ES中，使用的是Feign远程调用=======================
 * ES装数据的VO：GoodsVO
 * <p>
 * 此处就是调用interface中的接口方法，实现远程调用，获取在pms,sms。。。的数据
 */
@SpringBootTest
class GmallSearchApplicationTests {

    @Autowired
    private JestClient jestClient;
    @Autowired
    private GmallWmsClient gmallWmsClient;
    @Autowired
    private GmallPmsClient gmallPmsClient;

    @Test
    public void importData() {

        long pageNum = 1l;//初始的页码数
        long pageSize = 100l;//每一页有100条数据

        do {   //doWhile循环，就是当前页面的数据不足100，页面数就++
            QueryCondition condition = new QueryCondition();
            condition.setPage(pageNum);//设置当前页参数:页码数和总条数
            condition.setPage(pageSize);
            Resp<List<SpuInfoEntity>> listResp = gmallPmsClient.querySpuPage(condition);//??????????
            //获取当前页的spuInfo数据？？？？？
            List<SpuInfoEntity> spuInfoEntities = listResp.getData();//拿到当前页的信息

          /*  if (pageVo == null) {//1.如果当前页中的数据没有到100条，说明它就是最后一页
                pageSize = 0l;
                continue;  //如果上面的条件结束了，直接执行while处的语句了，就不执行下面的pageNum++(页数就不加了)
            }

            List<SpuInfoEntity> spuInfoEntities = (List<SpuInfoEntity>) pageVo.getList();*/
            //=========遍历spu 获取spu下的所有的sku，把sku的数据导入到索引库中==========
            for (SpuInfoEntity spuInfoEntity : spuInfoEntities) {
                Resp<List<SkuInfoEntity>> skuResp = this.gmallPmsClient.querySkuBySpuId(spuInfoEntity.getId());//远程调用Pms中的数据，通过spuId查询sku的信息
                List<SkuInfoEntity> skuInfoEntities = skuResp.getData();
                if(CollectionUtils.isEmpty(skuInfoEntities)){
                    continue;
                }
                for (SkuInfoEntity skuInfoEntity : skuInfoEntities) {//遍历上面拿到的skuInfoEntities,把其中的一个个的数据skuInfoEntity放到ES的goodsVO的库-表-id中
                    GoodsVO goodsVO = new GoodsVO();//创建ES的数据容器

                    //下面数据可以传到goodsVO中了，goodsVO还需要给拿到的数据分到页面上相应的地方
                    //A.设置sku相关数据：就是页面中sku商品的展示区         ==List<goods>
                    goodsVO.setName(skuInfoEntity.getSkuTitle());//title                 //使用skuInfoEntiry是因为skuInfoEntity ->才是一个个具体的数据库表对象数据
                    goodsVO.setId(skuInfoEntity.getSkuId());//id
                    goodsVO.setPic(skuInfoEntity.getSkuDefaultImg());//images
                    goodsVO.setPrice(skuInfoEntity.getPrice());
                    goodsVO.setSale(100);//默认销量
                    goodsVO.setSort(0);//排序，随便加一个





                    //B.设置分类相关                                       ==catlogVo
                    Resp<CategoryEntity> categoryEntityResp = gmallPmsClient.queryCategoryById(skuInfoEntity.getCatalogId());
                    CategoryEntity categoryEntity = categoryEntityResp.getData();
                    if (categoryEntity != null) {
                        goodsVO.setProductCategoryId(skuInfoEntity.getCatalogId());
                        goodsVO.setProductCategoryName(categoryEntity.getName());
                    }


                    //C.设置品牌相关                                      ==brand<VO>
                    Resp<BrandEntity> brandEntityResp = gmallPmsClient.querybrandById(skuInfoEntity.getBrandId());//通过brandId查询brand--》就是之前在pms中做的接口方法？？？？但是具体什么用不懂？？为什么用在这里？？？
                    BrandEntity brandEntity = brandEntityResp.getData();//拿到brand中的数据??上面一步不是拿到了brand的数据了吗？？？？？？
                    if (brandEntity != null) {
                        goodsVO.setBrandId(skuInfoEntity.getBrandId());
                        goodsVO.setBrandName(brandEntity.getName());
                    }

                    //D.设置搜索属性：检索属性                             ==list<VO>
                    Resp<List<SpuAttributeValueVO>> searchAttrValueResp = gmallPmsClient.querySearchAttrValue(spuInfoEntity.getId());
                    List<SpuAttributeValueVO> spuAttributeValueVOList = searchAttrValueResp.getData();
                    goodsVO.setAttrValueList(spuAttributeValueVOList);

                    //E.库存
                    Resp<List<WareSkuEntity>> resp = gmallWmsClient.queryWareBySkuId(skuInfoEntity.getSkuId());
                    List<WareSkuEntity> wareSkuEntityList = resp.getData();
                    if (wareSkuEntityList.stream().anyMatch(t -> t.getStock() > 0)) {//判断要是库存有货就用1表示   要是么有货就用0表示
                        goodsVO.setStock(1l);
                    } else {
                        goodsVO.setStock(0l);
                    }

                    Index index = new Index.Builder(goodsVO).index("goods").type("info").id(skuInfoEntity.getSkuId().toString()).build();//执行DSL语句，塞数据
                    try {
                        jestClient.execute(index);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            pageSize = Long.valueOf(spuInfoEntities.size());//2.获取当前页的记录数，如果pageSize满足100，就可以页码数++
            pageNum++;
        } while (pageSize == 100);//只有页面中数据是100条的情况下才能有下一页

    }

    @Test
    public void impordata2(){
        Long pageNum = 1l;
        Long pageSize = 100l;

        do {
            // 分页查询spu
            QueryCondition condition = new QueryCondition();
            condition.setPage(pageNum);
            condition.setLimit(pageSize);
            Resp<List<SpuInfoEntity>> listResp = this.gmallPmsClient.querySpuPage(condition);
            // 获取当前页的spuInfo数据
            List<SpuInfoEntity> spuInfoEntities = listResp.getData();

            // 遍历spu获取spu下的所有sku导入到索引库中
            for (SpuInfoEntity spuInfoEntity : spuInfoEntities) {
                Resp<List<SkuInfoEntity>> skuResp = this.gmallPmsClient.querySkuBySpuId(spuInfoEntity.getId());
                List<SkuInfoEntity> skuInfoEntities = skuResp.getData();
                if (CollectionUtils.isEmpty(skuInfoEntities)){
                    continue;
                }
                skuInfoEntities.forEach(skuInfoEntity -> {
                    GoodsVO goodsVO = new GoodsVO();

                    // 设置sku相关数据
                    goodsVO.setName(skuInfoEntity.getSkuTitle());
                    goodsVO.setId(skuInfoEntity.getSkuId());
                    goodsVO.setPic(skuInfoEntity.getSkuDefaultImg());
                    goodsVO.setPrice(skuInfoEntity.getPrice());
                    goodsVO.setSale(100); // 销量
                    goodsVO.setSort(0); // 综合排序

                    // 设置品牌相关的
                    Resp<BrandEntity> brandEntityResp = this.gmallPmsClient.querybrandById(skuInfoEntity.getBrandId());
                    BrandEntity brandEntity = brandEntityResp.getData();
                    if (brandEntity != null) {
                        goodsVO.setBrandId(skuInfoEntity.getBrandId());
                        goodsVO.setBrandName(brandEntity.getName());
                    }

                    // 设置分类相关的
                    Resp<CategoryEntity> categoryEntityResp = this.gmallPmsClient.queryCategoryById(skuInfoEntity.getCatalogId());
                    CategoryEntity categoryEntity = categoryEntityResp.getData();
                    if (categoryEntity != null) {
                        goodsVO.setProductCategoryId(skuInfoEntity.getCatalogId());
                        goodsVO.setProductCategoryName(categoryEntity.getName());
                    }

                    // 设置搜索属性
                    Resp<List<SpuAttributeValueVO>> searchAttrValueResp = this.gmallPmsClient.querySearchAttrValue(spuInfoEntity.getId());
                    List<SpuAttributeValueVO> spuAttributeValueVOList = searchAttrValueResp.getData();
                    goodsVO.setAttrValueList(spuAttributeValueVOList);

                    // 库存
                    Resp<List<WareSkuEntity>> resp = this.gmallWmsClient.queryWareBySkuId(skuInfoEntity.getSkuId());
                    List<WareSkuEntity> wareSkuEntities = resp.getData();
                    if (wareSkuEntities.stream().anyMatch(t -> t.getStock() > 0)) {
                        goodsVO.setStock(1l);
                    } else {
                        goodsVO.setStock(0l);
                    }

                    Index index = new Index.Builder(goodsVO).index("goods").type("info").id(skuInfoEntity.getSkuId().toString()).build();
                    try {
                        this.jestClient.execute(index);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });





            }

            pageSize = Long.valueOf(spuInfoEntities.size()); // 获取当前页的记录数
            pageNum++; // 下一页
        } while (pageSize == 100); // 循环条件
    }


    // @Test
    //void contextLoads() throws Exception {
       /*
       新增：index
       Index index = new Index.Builder(new User("通行百万", 18)).index("san").type("goods").id("2").build();

        jestClient.execute(index);//括号里面要一个action。就是什么操作（CRUD）*/

/*修改:一般不用。要更新数据直接使用index直接重新覆盖

        HashMap<String, Object> map = new HashMap<>();
        map.put("doc",new User("haha",10));
        Update update = new Update.Builder(map).index("san").type("info").id("1").build();//用map中的数据修改原来库：san。表：info.ID：1位置的内容
        DocumentResult result = jestClient.execute(update);
        System.out.println(result);
    }*/

//查询
       /* String query = "{\n" +
                "  \"query\":{\n" +
                "    \"match_all\":{}\n" +
                "  }\n" +
                "}";*/
       /* Search search = new Search.Builder(query).addIndex("san").build();//利用EDL的语句从san库里info表中查询结果
        SearchResult searchResult = jestClient.execute(search);
        System.out.println(searchResult.getSourceAsObject(User.class, false));//（1）第一种查看查询的信息方法。第一个参数：封装什么类型的数据。第二个：是都需要元数据
       //这是第二种方法可以获得更多的数据。就是把hits中的_source中的数据也拿出来；
        List<SearchResult.Hit<User, Void>> list = searchResult.getHits(User.class);
        list.forEach(hit -> {
            System.out.println(hit.score);//hit里面就有我们需要的数据（hit就是hits中的一段段数据，但是数据在_source中）
        });

    }*/
}

/*
@Data
@NoArgsConstructor
@AllArgsConstructor
class User {
    private String name;
    private Integer age;

}
*/
