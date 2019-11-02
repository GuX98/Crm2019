package com.mage.crm.base.exceptions;

import com.alibaba.fastjson.JSON;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.model.MessageModel;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class GlobalException implements HandlerExceptionResolver{
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse, Object handler, Exception e) {

        ModelAndView modelAndView = createDefaultModelAndView(httpServletRequest);
        ParamsException paramsException;
        if(handler instanceof HandlerMethod){

            //1是否是未登录异常
            if(e instanceof ParamsException){
                paramsException = (ParamsException) e;
                if(paramsException.getCode() == CrmConstant.LOGIN_NO_CODE){
                    modelAndView.addObject("code",paramsException.getCode());
                    modelAndView.addObject("msg",paramsException.getMsg());
                    return modelAndView;
                }
            }
            //Json异常
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
            if(null != responseBody){
                MessageModel messageModel = new MessageModel();
                messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
                messageModel.setMsg(CrmConstant.OPS_FAILED_MSG);
                if(e instanceof ParamsException){
                   paramsException = (ParamsException)e ;
                   messageModel.setCode(paramsException.getCode());
                   messageModel.setMsg(paramsException.getMsg());
                }
                httpServletResponse.setContentType("application/json;charset=uft-8");
                httpServletResponse.setCharacterEncoding("utf-8");
                PrintWriter printWriter = null;
                try {
                     printWriter = httpServletResponse.getWriter();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }finally {
                    if(printWriter!=null){
                        printWriter.write(JSON.toJSONString(messageModel));
                        printWriter.flush();
                        printWriter.close();
                    }
                }
                return null;
            }else{//试图异常
                if(e instanceof  ParamsException){
                   paramsException = (ParamsException)e;
                   modelAndView.addObject("code",paramsException.getCode());
                   modelAndView.addObject("msg",paramsException.getMsg());
                   return modelAndView;
                }else{
                    return modelAndView;
                }
            }
        }
        return null;
    }


    private static ModelAndView createDefaultModelAndView(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("code",CrmConstant.OPS_FAILED_CODE);
        modelAndView.addObject("msg",CrmConstant.OPS_FAILED_MSG);
        modelAndView.addObject("ctx",request.getContextPath());
        modelAndView.addObject("uri",request.getRequestURI());
        return modelAndView;
    }
}
