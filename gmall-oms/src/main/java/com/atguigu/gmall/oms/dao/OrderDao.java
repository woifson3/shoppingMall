package com.atguigu.gmall.oms.dao;

import com.atguigu.gmall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author gesanqiang
 * @email san@atguigu.com
 * @date 2019-10-29 16:33:14
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
