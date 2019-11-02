package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.CustomerGCQuery;
import com.mage.crm.query.CustomerQuery;
import com.mage.crm.service.CustomerService;
import com.mage.crm.vo.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController{

    @Resource
    private CustomerService customerService;
    /*
    查询所有的用户表中的用户
     */
    @ResponseBody
    @RequestMapping("queryAllCustomers")
    public List<Customer> queryAllCustomers(){

        return customerService.queryAllCustomers();
    }

    @RequestMapping("index")
    public String index(){
        return "customer";
    }

    @RequestMapping("queryCustomersByParams")
    @ResponseBody
    public Map<String,Object> queryCustomersByParams(CustomerQuery customerQuery){
        return customerService.queryCustomersByParams(customerQuery);
    }

    @ResponseBody
    @RequestMapping("insert")
    public MessageModel insert(Customer customer){
        customerService.insert(customer);
        return createMessageModel("客户信息添加成功！");
    }

    @ResponseBody
    @RequestMapping("update")
    public MessageModel update(Customer customer){
        customerService.update(customer);
        return createMessageModel("客户信息修改成功！");
    }

    @ResponseBody
    @RequestMapping("delete")
    public MessageModel delete(Integer[] id){
        customerService.delete(id);
        return createMessageModel("客户信息删除成功！");
    }

    @RequestMapping("openCustomerOtherInfo/{type}/{id}")
    public String openCustomerOtherInfo(@PathVariable("type") String type,
           @PathVariable("id") Integer id,Model model) {
        model.addAttribute("customer", customerService.queryCustomersById(id));
        switch (type) {
            case "1":
                return "customer_linkMan";
            case "2":
                return "customer_concat";
            case "3":
                return "customer_order";
            default:
                return "error";
        }
    }

    @ResponseBody
    @RequestMapping("queryCustomersContribution")
    public Map<String,Object> queryCustomersContribution(CustomerGCQuery customerGCQuery){
        return customerService.queryCustomersContribution(customerGCQuery);
    }

    @ResponseBody
    @RequestMapping("queryCustomerGC")
    public Map<String,Object> queryCustomerGC(){
        return customerService.queryCustomerGC();
    }

}
