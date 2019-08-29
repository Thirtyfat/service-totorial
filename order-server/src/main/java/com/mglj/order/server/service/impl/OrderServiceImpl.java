package com.mglj.order.server.service.impl;

import com.mglj.order.server.dao.mapper.OrderMapper;
import com.mglj.order.server.domain.Order;
import com.mglj.order.server.service.api.OrderService;
import com.mglj.totorial.framework.gid.service.api.GidService;
import com.mglj.totorial.framework.tools.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by yj on 2019/8/29.
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private GidService gidService;

    @Override
    public Result<Integer> insertSelective() {
        Result<Integer> result = new Result<>();
        Order record = new Order();
        record.setId(gidService.generate());
        record.setUserId(System.currentTimeMillis());
        record.setUserName(System.currentTimeMillis() + "i");
        record.setCreationTime(new Date());
        result.setData(orderMapper.insertSelective(record));
        return result;
    }

    @Override
    public Result<Order> selectByPrimaryKey(Long id) {
        Result<Order> result = new Result<>();
        Order order = orderMapper.selectByPrimaryKey(id);
        result.setData(order);
        return result;
    }


}
