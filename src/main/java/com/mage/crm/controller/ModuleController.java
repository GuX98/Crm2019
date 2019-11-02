package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.dto.ModuleDto;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.service.ModuleService;
import com.mage.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController{
    @Resource
    private ModuleService moduleService;

    @RequestMapping("index")
    public String index(){
    return "module";
    }
    @RequestMapping("queryAllsModuleDtos")
    @ResponseBody
    public List<ModuleDto> queryAllsModuleDtos(Integer rid){
        return moduleService.queryAllsModuleDtos(rid);
    }

    @RequestMapping("queryModulesByParams")
    @ResponseBody
    public Map<String,Object> queryModulesByParams(ModuleQuery moduleQuery){
        return moduleService.queryModulesByParams(moduleQuery);
    }

    @RequestMapping("queryModulesByGrade")
    @ResponseBody
    public List<Module> queryModulesByGrade(Integer grade){
        return moduleService.queryModulesByGrade(grade);
    }

    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(Module module){
        moduleService.insert(module);
        return createMessageModel("模块记录添加成功！");
    }

    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(Module module){
        moduleService.update(module);
        return createMessageModel("模块记录更新成功！");
    }

    @RequestMapping("delete")
    @ResponseBody
    public MessageModel delete(Integer[] ids){
        moduleService.delete(ids[0]);
        return createMessageModel("模块记录删除成功！");
    }
}
