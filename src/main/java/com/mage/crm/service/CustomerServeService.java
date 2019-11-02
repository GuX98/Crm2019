package com.mage.crm.service;

import com.mage.crm.dao.CustomerServeDao;
import com.mage.crm.dto.ServeTypeDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServeService {

    @Resource
    private CustomerServeDao customerServeDao;

    public Map<String,Object> queryCustomerServeType() {
        List<ServeTypeDto> serveTypeDtoList=customerServeDao.queryCustomerServeType();
        Map<String, Object> map = new HashMap<>();
        map.put("code",300);
        String[] types;
        ServeTypeDto[] datas;
        if (serveTypeDtoList!=null &&serveTypeDtoList.size()>0){
            types=new String[serveTypeDtoList.size()];
            datas=new ServeTypeDto[serveTypeDtoList.size()];
            for (int i=0;i<serveTypeDtoList.size();i++){
                types[i]=serveTypeDtoList.get(i).getName();
                datas[i]=serveTypeDtoList.get(i);
            }
            map.put("code",200);
            map.put("types",types);
            map.put("datas",datas);
        }
        return map;
    }
}
