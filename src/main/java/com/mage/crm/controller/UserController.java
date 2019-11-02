package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.base.exceptions.ParamsException;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.UserRoleDao;
import com.mage.crm.model.MessageModel;
import com.mage.crm.model.UserModel;
import com.mage.crm.query.UserQuery;
import com.mage.crm.service.UserService;
import com.mage.crm.util.CookieUtil;

import com.mage.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;



    @RequestMapping("userLogin")
    @ResponseBody
    public MessageModel userLogin(String userName, String userPwd){
        MessageModel messageModel = new MessageModel();
        UserModel userModel = userService.userLogin(userName, userPwd);
        messageModel.setResult(userModel);
    return messageModel;
    }

    @RequestMapping("updatePwd")
    @ResponseBody
    public MessageModel updatePwd(HttpServletRequest request,String
            oldPassword,String newPassword,String confirmPassword){
        MessageModel messageModel = new MessageModel();
        String id = CookieUtil.getCookieValue(request, "userId");
        try{
            userService.updatePwd(id,oldPassword,newPassword,confirmPassword);
            messageModel.setMsg("密码修改成功！请重新登录");
        }catch (ParamsException e){
            e.printStackTrace();
            messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
            messageModel.setMsg(e.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
            messageModel.setMsg(CrmConstant.OPS_FAILED_MSG);
        }
       return messageModel;
    }

    @RequestMapping("queryAllCustomerManager")
    @ResponseBody
    public List<User> queryAllCustomerManager(){
        return userService.queryAllCustomerManager();
    }

    @RequestMapping("index")
    public String index(){
        return "user";
    }

    @RequestMapping("queryUsersByParams")
    @ResponseBody
    public Map<String,Object> queryUsersByParams(UserQuery userQuery){
        return userService.queryUsersByParams(userQuery);
    }

    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(User user){
        userService.insert(user);
        return createMessageModel("用户添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(User user){
        userService.update(user);
        return createMessageModel("用户信息修改成功！");
    }

}
