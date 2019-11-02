package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.dao.ModuleDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Module;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ModuleService {
    @Resource
    private ModuleDao moduleDao;
    @Resource
    private PermissionDao permissionDao;

    public List<ModuleDto> queryAllsModuleDtos(Integer rid) {
        //查询到所有的资源
        List<ModuleDto> moduleDtos = moduleDao.queryAllsModuleDtos(rid);
        //做一个勾选的问题
        //查询permssion根据rid得到modelId
        List<Integer> moduleIds =  permissionDao.queryPermissionModuleIdsByRid(rid);// modelid 1 2 3 4 5 6
        if(moduleIds!=null && moduleIds.size()>0){
            for(ModuleDto moduleDto : moduleDtos){
                Integer id = moduleDto.getId();
                if(moduleIds.contains(id)){
                    moduleDto.setChecked(true);
                }
            }
        }
        return moduleDtos;
    }

    public Map<String,Object> queryModulesByParams(ModuleQuery moduleQuery) {
        PageHelper.startPage(moduleQuery.getPage(), moduleQuery.getRows());
        List<Module> modules = moduleDao.queryModulesByParams(moduleQuery);
        PageInfo<Module> pageInfo = new PageInfo<>(modules);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pageInfo.getTotal());
        map.put("rows", pageInfo.getList());
        return map;
    }

    public List<Module> queryModulesByGrade(Integer grade) {
        return moduleDao.queryModulesByGrade(grade);
    }

    public void insert(Module module) {
        checkModuleParams(module.getModuleName(), module.getGrade(), module.getOptValue());
        AssertUtil.isTrue(null!=moduleDao.queryModuleByOptValue(module.getOptValue()),"权限值不能重复!");
        AssertUtil.isTrue(null!=moduleDao.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName()),
                "该层级下模块名已存在!");
        if(module.getGrade()!=0){
            AssertUtil.isTrue(null == moduleDao.queryModuleByPid(module.getParentId()),"父级菜单不存在！");
        }
        module.setIsValid(1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleDao.insert(module)<1, CrmConstant.OPS_FAILED_MSG);
    }

    private void checkModuleParams(String moduleName, Integer grade,
        String optValue) {
        AssertUtil.isTrue(StringUtils.isBlank(moduleName), "模块名非空!");
        AssertUtil.isTrue(null == grade, "层级值非法!");
        Boolean flag = (grade != 0 && grade != 1 && grade != 2);
        AssertUtil.isTrue(flag, "层级值非法!");
        AssertUtil.isTrue(StringUtils.isBlank(optValue), "权限值非空!");
    }
    private void checkModuleParams(String moduleName, Integer grade,
                                   String optValue, Integer id) {
        checkModuleParams(moduleName, grade, optValue);
        AssertUtil.isTrue(null == id || null == moduleDao.queryModuleById(id), "待更新模块不存在!");
    }

    public void update(Module module) {
        checkModuleParams(module.getModuleName(),module.getGrade(),module.getOptValue(),module.getId());
        Module temp = moduleDao.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null!=temp && !temp.getId().equals(module.getId()),"权限值不能重复！");
        temp = moduleDao.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName());
        AssertUtil.isTrue(null!=temp&&!temp.getId().equals(module.getId()),"该层级下模块名不能重复！");
        if(module.getGrade()!=0){
            AssertUtil.isTrue(null==moduleDao.queryModuleByPid(module.getParentId()),"父级菜单不存在！");
        }
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleDao.update(module)<1,CrmConstant.OPS_FAILED_MSG);
    }

    public void delete(Integer id) {
        AssertUtil.isTrue(null == id || null == moduleDao.queryModuleById(id),"待删除记录不存在！");
        List<Integer> mids = new ArrayList<Integer>();
        mids = getSubModuleIds(id,mids);
        AssertUtil.isTrue(moduleDao.delete(mids) < mids.size(), CrmConstant.OPS_FAILED_MSG);
    }

    private List<Integer> getSubModuleIds(Integer id, List<Integer> mids) {
        Module module = moduleDao.queryModuleById(id);
        if(module != null){
            mids.add(module.getId());//添加到待删除的集合中
            //查询子记录
            List<Module> modules = moduleDao.querySubModulesByPid(module.getId());
            if(null!=modules && modules.size()>0){
                for(Module temp : modules){
                    mids = getSubModuleIds(temp.getId(),mids);
                }
            }
        }
        return mids;
    }
}
