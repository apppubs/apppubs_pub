package com.hingecloud.apppubs.pub.model.dto;

import com.hingecloud.apppubs.pub.model.TTask;
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
    private String storePassword;
    private String keyAlias;
    private String keyPassword;
    private String callback;
    private String iosBundleId;
    private String iosCerPassword;
    private MultipartFile assets;

    @Override
    public void validate() {
        ValidateHelper.notNull(getAppName(), "appName为空！");
        ValidateHelper.notNull(getAppId(), "appId为空！");
        ValidateHelper.notNull(getBaseURL(), "baseURL为空！");
        ValidateHelper.notNull(getType(), "type为空！");
        ValidateHelper.notNull(getVersionName(), "versionName为空！");
        ValidateHelper.notNull(getAssets(), "assets为空！");
        ValidateHelper.notNull(getCallback(), "callback为空！");
        ValidateHelper.isTrue(TTask.TYPE_ANDROID.equals(type) || TTask.TYPE_IOS.equals(type), "类型type=" + type + " 错误（必须为android或者ios）");
        if (type.equals(TTask.TYPE_ANDROID)) {
            ValidateHelper.notNull(getPackageName(), "packageName为空!");
        } else {
            ValidateHelper.notBlank(getIosBundleId(), "bundleId 为空！");
        }
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

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getIosBundleId() {
        return iosBundleId;
    }

    public void setIosBundleId(String iosBundleId) {
        this.iosBundleId = iosBundleId;
    }

    public String getIosCerPassword() {
        return iosCerPassword;
    }

    public void setIosCerPassword(String iosCerPassword) {
        this.iosCerPassword = iosCerPassword;
    }
}
