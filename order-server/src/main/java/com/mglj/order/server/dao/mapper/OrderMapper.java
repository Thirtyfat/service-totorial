package com.mglj.order.server.dao.mapper;



import com.mglj.order.server.domain.Order;
import org.springframework.stereotype.Component;



@Component
public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}