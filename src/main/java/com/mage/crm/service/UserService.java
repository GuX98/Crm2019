package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.UserDao;
import com.mage.crm.dao.UserRoleDao;
import com.mage.crm.dto.CustomerDto;
import com.mage.crm.model.UserModel;
import com.mage.crm.query.UserQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.Md5Util;
import com.mage.crm.vo.Customer;
import com.mage.crm.vo.User;
import com.mage.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private HttpSession session;
    @Resource
    private PermissionDao permissionDao;

    /*
    登录验证
     */
    public UserModel userLogin(String userName,String userPwd){
        //查询用户
        User user = userDao.queryUserByName(userName);
        //判断是否查询到用户
        AssertUtil.isTrue(user==null,"用户不存在");
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已经被注销");
        //判断密码
        String pwd = Md5Util.encode(userPwd);
        AssertUtil.isTrue(!pwd.equals(user.getUserPwd()),"密码错误");

        /**
         * 获取用户权限  根据用户拥有的角色
         */
        List<String> permissions = permissionDao.queryPermissionsByUserId(user.getId()+"");
        if(permissions!=null && permissions.size()>0){
            session.setAttribute("userPermission", permissions);
        }
        return createUserModel(user);
}

    public UserModel createUserModel(User user){
        UserModel userModel = new UserModel();
        userModel.setTrueName(user.getTrueName());
        userModel.setUserName(user.getUserName());

        String id = Base64Util.enCode(user.getId());
        userModel.setUserId(id);
        return userModel;
    }

    /*
    修改密码
     */
    public void updatePwd(String id,String oldPassword,
                          String newPassword,String confirmPassword){
        AssertUtil.isTrue(StringUtils.isBlank(id),"id不存在");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码与原密码一致");
        AssertUtil.isTrue(!newPassword.equals(confirmPassword),"确认密码与新密码不一致");

        User user = userDao.queryUserById(Base64Util.deCode(id));
        AssertUtil.isTrue(null==user,"用户不存在了");
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已经被注销了");
        AssertUtil.isTrue(!Md5Util.encode(oldPassword).equals(user.getUserPwd()),"原始密码错误");
        AssertUtil.isTrue(userDao.updatePwd(user.getId(),Md5Util.encode(newPassword))<1,"用户密码更新失败");
    }

    public User queryUserById(String id){
        return userDao.queryUserById(id);
    }

    public List<User> queryAllCustomerManager() {
        return userDao.queryAllCustomerManager();
    }

    public Map<String,Object> queryUsersByParams(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPage(),userQuery.getRows());
        List<User> list = userDao.queryUsersByParams(userQuery);
        PageInfo<User> userPageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total",userPageInfo.getTotal());
        map.put("rows",userPageInfo.getList());
        return map;
    }

    public void insert(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getTrueName()),"真实姓名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()),"手机号码不能为空");
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setIsValid(1);
        user.setUserPwd(Md5Util.encode("123456"));

        User user1 = userDao.queryUserByName(user.getUserName());
        AssertUtil.isTrue(user1!=null&&user1.getUserName().equals(user.getUserName()),"不能有相同用户名");
        AssertUtil.isTrue(userDao.insert(user)<1,"添加用户失败！");

        List<Integer> roleIds = user.getRoleIds();
        if(roleIds!=null&&roleIds.size()>0){
            relateRoles(roleIds,Integer.parseInt(user.getId()));
        }
    }

    private void relateRoles(List<Integer> roleIds, int userId){
        List<UserRole> roleList = new ArrayList<>();
        for(Integer roleId:roleIds){
            UserRole userRole = new UserRole();
            userRole.setIsValid(1);
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            roleList.add(userRole);
        }
        AssertUtil.isTrue(userRoleDao.insertBatch(roleList)<1,"用户角色添加失败");
    }

    public void update(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getTrueName()),"真实姓名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()),"手机号码不能为空");
        user.setUpdateDate(new Date());
        User u = userDao.queryUserByName(user.getUserName());
        AssertUtil.isTrue(u!=null&&!u.getId().equals(user.getId()),"不能有相同用户名");
        AssertUtil.isTrue(userDao.update(user)<1,"用户修改失败");
        List<Integer> roleIds = user.getRoleIds();
        if(roleIds!=null&&roleIds.size()>0){
            //先删除，在插入
            int count = userRoleDao.queryRoleCountsByUserId(user.getId());
            if(count>0){
                AssertUtil.isTrue(userRoleDao.deleteRolesByUserId(user.getId())<count,"用户修改失败");
            }
            relateRoles(roleIds,Integer.parseInt(user.getId()));
        }
    }
}
