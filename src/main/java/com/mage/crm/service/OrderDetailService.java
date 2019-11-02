package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.OrderDetailDao;
import com.mage.crm.query.OrderDetailQuery;
import com.mage.crm.vo.CustomerOrder;
import com.mage.crm.vo.OrderDetail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderDetailService {
    @Resource
    private OrderDetailDao orderDetailDao;

    public Map<String,Object> queryOrderDetailsByOrderId(OrderDetailQuery orderDetailQuery) {
        PageHelper.startPage(orderDetailQuery.getPage(),orderDetailQuery.getRows());
        List<OrderDetail> list = orderDetailDao.queryOrderDetailsByOrderId(orderDetailQuery);
        PageInfo<OrderDetail> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows",pageInfo.getList());
        map.put("total",pageInfo.getTotal());
        return map;
    }
}
