package com.atguigu.gmall.sms.dao;

import com.atguigu.gmall.sms.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author gesanqiang
 * @email san@atguigu.com
 * @date 2019-10-28 21:02:18
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
