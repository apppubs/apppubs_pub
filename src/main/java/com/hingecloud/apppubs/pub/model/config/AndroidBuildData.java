package com.hingecloud.apppubs.pub.model.config;

import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.utils.StringUtil;

public class AndroidBuildData {
    private String packageName;
    private String appName;
    private String wxAppId;
    private String appId;
    private String baseURL;
    private String jpushAppKey;
    private String versionName;
    private String versionCode;
    private String storePassword;
    private String keyAlias;
    private String keyPassword;

    public static AndroidBuildData createFromTask(TTask task) {
        AndroidBuildData data = new AndroidBuildData();
        data.setPackageName(task.getPackageName());
        data.setAppName(task.getAppName());
        data.setWxAppId(StringUtil.getString(task.getWxAppId(), ""));
        data.setAppId(task.getAppId());
        data.setBaseURL(task.getBaseUrl());
        data.setJpushAppKey(StringUtil.getString(task.getJpushAppId(), ""));
        data.setVersionName(task.getVersionName());
        data.setVersionCode(task.getVersionCode() + "");
        data.setStorePassword(task.getStorePassword());
        data.setKeyAlias(task.getKeyAlias());
        data.setKeyPassword(task.getKeyPassword());
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

    public String getStorePassword() {
        return storePassword;
    }

    public void setStorePassword(String storePassword) {
        this.storePassword = storePassword;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }
}
