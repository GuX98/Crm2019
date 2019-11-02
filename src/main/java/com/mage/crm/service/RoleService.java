package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.dao.ModuleDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.RoleDao;
import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.RoleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Module;
import com.mage.crm.vo.Permission;
import com.mage.crm.vo.Role;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService {
    @Resource
    private RoleDao roleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private ModuleDao moduleDao;

    public List<Role> queryAllRoles() {
        return roleDao.queryAllRoles();
    }

    public  Map<String,Object> queryRolesByParams(RoleQuery roleQuery) {
        PageHelper.startPage(roleQuery.getPage(), roleQuery.getRows());
        List<Role> roles= roleDao.queryRolesByParams(roleQuery.getRoleName());
        PageInfo<Role> pageInfo=new PageInfo<Role>(roles);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("total", pageInfo.getTotal());
        map.put("rows", pageInfo.getList());
        return map;
    }

    public void addPermission(Integer rid, Integer[] moduleIds) {
        //授权角色存在判断
        AssertUtil.isTrue(rid==null||null==roleDao.queryRoleById(rid+""),"授权角色不存在");
        //查询权限个数
        int count = permissionDao.queryPermissionCountByRid(rid);
        //删除原有的权限
        if(count>0){
            AssertUtil.isTrue(permissionDao.deletePermissionByRid(rid)<count, CrmConstant.OPS_FAILED_MSG);
        }
        List<Permission> permissions = null;
        //判断前台传来的moduleIds是否为空
        if(null!=moduleIds && moduleIds.length>0){
            //批量添加
            permissions = new ArrayList<>();
            Module module = null;
            //循环遍历
            for(Integer moduleId : moduleIds){
                module = moduleDao.queryModuleById(moduleId);
                //组装per
                Permission permission = new Permission();
                if(module!=null){
                    permission.setAclValue(module.getOptValue());
                }
                permission.setRoleId(rid);
                permission.setModuleId(moduleId);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                //放到List集合中
                permissions.add(permission);
            }
            AssertUtil.isTrue(permissionDao.insertBatch(permissions)<moduleIds.length,CrmConstant.OPS_FAILED_MSG);
        }
    }
}
