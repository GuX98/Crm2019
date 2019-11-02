package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.service.CustomerServeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_serve")
public class CustomerServeController extends BaseController{

    @Resource
    private CustomerServeService customerServeService;

    @RequestMapping("queryCustomerServeType")
    @ResponseBody
    public Map<String,Object> queryCustomerServeType(){
        return customerServeService.queryCustomerServeType();
    }
}
