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
     * 生成16位的UUID
     * @return
     */
    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0     
        // 4 代表长度为4     
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

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
