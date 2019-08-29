package com.mglj.order.server.controller.order;

import com.mglj.order.server.service.api.OrderService;
import com.mglj.totorial.framework.tools.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yj on 2019/8/29.
 **/
@RestController("/order")
public class OrderController {

    public OrderController(){
        System.out.println("OrderController ==> " + this);
        System.out.println("RestController ==>"  + RestController.class);
    }

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/save",method = RequestMethod.GET)
    public Result<?> saveOrder() {
        return orderService.insertSelective();
    }

}
