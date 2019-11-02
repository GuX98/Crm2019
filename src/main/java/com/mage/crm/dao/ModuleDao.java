package com.mage.crm.dao;

import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.vo.Module;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ModuleDao {
    @Select("SELECT m.id,m.module_name as 'name',m.parent_id  as 'pId'from t_module m where is_valid=1")
    List<ModuleDto> queryAllsModuleDtos(Integer rid);

    @Select("select id,module_name as moduleName,opt_value as optValue"
            + " from t_module where id=#{mid} and is_valid=1")
    Module queryModuleById(Integer moduleId);


    List<Module> queryModulesByParams(ModuleQuery moduleQuery);

    @Select("select id,module_name as moduleName from t_module where is_valid=1 and grade=#{grade}")
    List<Module> queryModulesByGrade(Integer grade);

    @Select("select  id,module_name as moduleName,url,opt_value as optValue"
            + "  from t_module where opt_value=#{optValue} and is_valid=1")
    Module queryModuleByOptValue(@Param("optValue") String optValue);

    @Select("select id,module_name as moduleName,url,opt_value as optValue"
            + " from t_module where grade=#{grade} and module_name=#{moduleName} and is_valid=1" )
    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName")String moduleName);

    @Select("select id,module_name as moduleName,url,opt_value as optValue"
            + " from t_module where id=#{pid} and is_valid=1" )
    Module queryModuleByPid(Integer parentId);

    @Insert("insert into t_module(module_name,module_style,url,opt_value,parent_id,grade,"
            + "orders,is_valid,create_date,update_date) values("
            + "#{moduleName},#{moduleStyle},#{url},#{optValue},#{parentId},#{grade},"
            + "#{orders},#{isValid},#{createDate},#{updateDate})")
    int insert(Module module);

    @Update("update t_module set module_name=#{moduleName},module_style=#{moduleStyle},"
            + "url=#{url},opt_value=#{optValue},parent_id=#{parentId},"
            + "grade=#{grade},orders=#{orders},update_date=#{updateDate}"
            + "where id=#{id} and is_valid=1")
    int update(Module module);

    @Select("select id,module_name as moduleName "
            + " from t_module where parent_id=#{pid} and is_valid=1")
    List<Module> querySubModulesByPid(Integer id);

    int delete(List<Integer> ids);
}
