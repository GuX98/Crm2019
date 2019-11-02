package com.mage.crm.dao;

import com.mage.crm.query.OrderDetailQuery;
import com.mage.crm.vo.OrderDetail;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderDetailDao {

    @Select("SELECT\n" +
            "\tid 'id',\n" +
            "order_id 'orderId',\n" +
            "goods_name 'goodsName',\n" +
            "unit 'unit',\n" +
            "price 'price',\n" +
            "sum 'sum'\n" +
            "\n" +
            "FROM\n" +
            "\t`t_order_details` \n" +
            "WHERE\n" +
            "\torder_id = #{orderId}")
    List<OrderDetail> queryOrderDetailsByOrderId(OrderDetailQuery orderDetailQuery);
}
