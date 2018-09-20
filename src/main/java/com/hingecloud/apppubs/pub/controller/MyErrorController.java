package com.hingecloud.apppubs.pub.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == 401){
            return "401";
        }else if(statusCode == 404){
            return "404";
        }else if(statusCode == 403){
            return "403";
        }else{
            return "500";
        }
    }

    @Override
    public String getErrorPath() {
        return "error";
    }
}
