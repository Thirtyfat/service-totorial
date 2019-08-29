package com.mglj.order.server.service.api;

import com.mglj.order.server.domain.Order;
import com.mglj.totorial.framework.tools.model.Result;

/**
 * Created by yj on 2019/8/29.
 **/
public interface OrderService {

    Result<Order> selectByPrimaryKey(Long id);
    Result<Integer> insertSelective();
}
