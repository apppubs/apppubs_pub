package com.hingecloud.apppubs.pub.tools;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

import java.io.Serializable;

public class JsonResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码：@see CodeConst
     */
    private int code = CodeConst.SUCCESS;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 结果对象
     */
    private Object result;

    private JsonResult() {
    }

    public static JsonResult success(Object result) {
        return new JsonResult().setResult(result);
    }

    public static JsonResult success(Object result, String msg) {
        JsonResult Jresult = new JsonResult();
        Jresult.setMsg(msg);
        Jresult.setResult(result);
        return Jresult;
    }

    public static JsonResult failure(int code, String msg) {
        return new JsonResult().setCode(code).setMsg(msg);
    }

    public static JsonResult failure(String code, String msg) {
        Assert.isTrue(StringUtils.isNumeric(code), "错误码格式不正确");
        return failure(Integer.parseInt(code), msg);
    }

    public static JsonResult generic() {
        return generic(StringUtils.EMPTY);
    }

    public static JsonResult generic(String msg) {
        return failure(CodeConst.GENERIC, StringUtils.defaultIfBlank(msg, "系统繁忙,请稍后再试"));
    }

    public static JsonResult illegalArgument(String msg) {
        return failure(CodeConst.ILLEGAL_ARGUMENT, StringUtils.defaultIfBlank(msg, "参数不合法"));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public int getCode() {
        return code;
    }

    public JsonResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public JsonResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public JsonResult setResult(Object result) {
        this.result = result;
        return this;
    }

}
