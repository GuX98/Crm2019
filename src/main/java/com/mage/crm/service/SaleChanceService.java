package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.SaleChanceDao;
import com.mage.crm.query.SaleChanceQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService {

    @Resource
    private SaleChanceDao saleChanceDao;


    public Map<String,Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery) {
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getRows());
        List<SaleChance> saleChanceList=saleChanceDao.querySaleChancesByParams(saleChanceQuery);
        PageInfo<SaleChance> saleChancePageInfo = new PageInfo<>(saleChanceList);
        List<SaleChance> list = saleChancePageInfo.getList();
        HashMap<String, Object> map = new HashMap<>();
        map.put("rows",list);
        map.put("total",saleChancePageInfo.getTotal());
        return map;
    }

    public void insert(SaleChance saleChance) {
        checkParams(saleChance.getLinkMan(),saleChance.getLinkPhone(),saleChance.getCgjl());
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        saleChance.setDevResult(0);//开发结果 0-3代表 未开发 开发中 开发成功 开发失败
        if(StringUtils.isBlank(saleChance.getAssignMan())){//未分配
            saleChance.setState(0);
        }else{//已分配
            saleChance.setState(1);
        }
        AssertUtil.isTrue(saleChanceDao.insert(saleChance)<1,"添加失败");
    }

    //非空校验
    private void checkParams(String linkMan,String linkPhone,String cgjl){
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(cgjl),"成功几率不能为空");
    }

    public void update(SaleChance saleChance) {
        checkParams(saleChance.getLinkMan(),saleChance.getLinkPhone(),saleChance.getCgjl());
        saleChance.setUpdateDate(new Date());
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
        }else{
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
        }
        AssertUtil.isTrue(saleChanceDao.update(saleChance)<1,"数据更新失败");
    }

    public void delete(Integer[] id) {
        AssertUtil.isTrue(saleChanceDao.delete(id)<id.length,"删除失败！");
    }

    public SaleChance querySaleChancesById(Integer id) {
        return saleChanceDao.querySaleChancesById(id);
    }


    public void updateSaleChanceDevResult(Integer devResult, Integer saleChanceId) {
        saleChanceDao.updateDevResult(saleChanceId,devResult);
    }
}
