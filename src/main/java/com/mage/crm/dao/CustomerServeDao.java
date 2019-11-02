package com.mage.crm.dao;

import com.mage.crm.dto.ServeTypeDto;

import java.util.List;

public interface CustomerServeDao {
    List<ServeTypeDto> queryCustomerServeType();
}
