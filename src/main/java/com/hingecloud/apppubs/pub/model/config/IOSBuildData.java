package com.hingecloud.apppubs.pub.model.config;

import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.utils.StringUtil;

public class IOSBuildData {

    private String bundleId;
    private String appName;
    private String wxAppId;
    private String appId;
    private String baseURL;
    private String jpushAppKey;
    private String versionName;
    private String versionCode;
    private String cerPassword;


    public static IOSBuildData createFromTask(TTask task) {
        IOSBuildData data = new IOSBuildData();
        data.setBundleId(task.getBundleId());
        data.setAppName(task.getAppName());
        data.setWxAppId(task.getWxAppId());
        data.setAppId(task.getAppId());
        data.setBaseURL(task.getBaseUrl());
        data.setJpushAppKey(task.getJpushAppId());
        data.setVersionName(task.getVersionName());
        data.setVersionCode(task.getVersionCode() + "");
        data.setCerPassword(StringUtil.isEmpty(task.getCerPassword()) ? "1234" : task.getCerPassword());
        return data;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
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

    public String getJpushAppKey() {
        return jpushAppKey;
    }

    public void setJpushAppKey(String jpushAppKey) {
        this.jpushAppKey = jpushAppKey;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getCerPassword() {
        return cerPassword;
    }

    public void setCerPassword(String cerPassword) {
        this.cerPassword = cerPassword;
    }
}
