package com.hingecloud.apppubs.pub.controller;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/*import com.com.heaven.huolinhe.model.VUserMenu;
import com.result.JsonResult;*/


public abstract class BaseController {
//    protected JsonResult retContent(int status, Object data) {
//        return ReturnFormat.retParam(status, data);
//    }
    
    /**
     * 获取session信息
     * @param name
     * @return
     */
    public static Object getWebUserAttribute(String name) {
		// TODO Auto-generated method stub
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
		Object obj=null;
		if (!name.trim().equals("")) {
			obj= request.getSession().getAttribute(name);
		}
		return obj;
	}
}
