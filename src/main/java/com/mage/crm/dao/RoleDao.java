package com.mage.crm.dao;

import com.mage.crm.vo.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleDao {

    List<Role> queryAllRoles();

    List<Role> queryRolesByParams(String roleName);

    @Select("select id,role_name as roleName,role_remark as roleRemark "
            + "from t_role " +
            "where id=#{id} and is_valid=1")
    Role queryRoleById(String s);
}
