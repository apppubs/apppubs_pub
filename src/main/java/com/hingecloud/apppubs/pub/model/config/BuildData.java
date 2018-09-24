package com.hingecloud.apppubs.pub.model.config;

import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.utils.StringUtil;

public class BuildData {
    private String packageName;
    private String appName;
    private String wxAppId;
    private String appId;
    private String baseURL;
    private String jpushAppKey;
    private String versionName;
    private String versionCode;

    public static BuildData createFromTask(TTask task) {
        BuildData data = new BuildData();
        data.setPackageName(task.getPackageName());
        data.setAppName(task.getAppName());
        data.setWxAppId(StringUtil.getString(task.getWxAppId(),""));
        data.setAppId(task.getAppId());
        data.setBaseURL(task.getBaseURL());
        data.setJpushAppKey(StringUtil.getString(task.getJpushAppId(),""));
        data.setVersionName(task.getVersionName());
        data.setVersionCode(task.getVersionCode() + "");
        return data;
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
}
