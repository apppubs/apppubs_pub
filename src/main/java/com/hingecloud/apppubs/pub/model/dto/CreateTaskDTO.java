package com.hingecloud.apppubs.pub.model.dto;

import com.hingecloud.apppubs.pub.tools.ValidateHelper;
import org.springframework.web.multipart.MultipartFile;

public class CreateTaskDTO extends AbsDTO {

    private String packageName;
    private String appName;
    private String appId;
    private String baseURL;
    private String type;
    private String versionName;
    private String wxAppId;
    private String jpushAppId;
    private MultipartFile assets;

    @Override
    public void validate() {
        ValidateHelper.notNull(getPackageName(), "packageName为空!");
        ValidateHelper.notNull(getAppName(), "appName为空！");
        ValidateHelper.notNull(getAppId(),"appId为空！");
        ValidateHelper.notNull(getBaseURL(),"baseURL为空！");
        ValidateHelper.notNull(getType(),"type为空！");
        ValidateHelper.notNull(getVersionName(),"versionName为空！");
        ValidateHelper.notNull(getAssets(),"assets为空！");
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getJpushAppId() {
        return jpushAppId;
    }

    public void setJpushAppId(String jpushAppId) {
        this.jpushAppId = jpushAppId;
    }

    public MultipartFile getAssets() {
        return assets;
    }

    public void setAssets(MultipartFile assets) {
        this.assets = assets;
    }
}
