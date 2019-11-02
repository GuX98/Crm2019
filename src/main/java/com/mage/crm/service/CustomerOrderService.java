package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerOrderDao;
import com.mage.crm.query.CustomerOrderQuery;
import com.mage.crm.vo.CustomerOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOrderService {

    @Resource
    private CustomerOrderDao customerOrderDao;

    public Map<String,Object> queryOrdersByCid(CustomerOrderQuery customerOrderQuery) {
        PageHelper.startPage(customerOrderQuery.getPage(),customerOrderQuery.getRows());
        List<CustomerOrder> list = customerOrderDao.queryOrdersByCid(customerOrderQuery);
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows",pageInfo.getList());
        map.put("total",pageInfo.getTotal());
        return map;
    }

    public Map<String,Object> queryOrderInfoById(Integer orderId) {
        return customerOrderDao.queryOrderInfoById(orderId);
    }
}
