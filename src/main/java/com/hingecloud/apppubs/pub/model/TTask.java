package com.hingecloud.apppubs.pub.model;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 实体类
 * </p>
 *
 * @author 张稳
 * @since 2018-09-20
 */
@TableName("task")
public class TTask implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_WAITING = 0;
    public static final int STATUS_BUILDING = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_CANCEL = 4;

    public static final String TYPE_ANDROID = "android";
    public static final String TYPE_IOS = "ios";


    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    @TableField("app_id")
    private String appId;
    @TableField("package_name")
    private String packageName;
    @TableField("app_name")
    private String appName;
    @TableField("base_url")
    private String baseUrl;
    private String type;
    @TableField("version_name")
    private String versionName;
    @TableField("version_code")
    private Integer versionCode;
    @TableField("wx_app_id")
    private String wxAppId;
    @TableField("jpush_app_id")
    private String jpushAppId;
    private String assets;
    @TableField("enable_splash_skip")
    private Integer enableSplashSkip;
    @TableField("enable_start_up_version")
    private Integer enableStartUpVersion;
    private Integer status = STATUS_WAITING;
    @TableField("create_time")
    private Date createTime;
    @TableField("download_url")
    private String downloadUrl;
    @TableField("store_password")
    private String storePassword;
    @TableField("key_alias")
    private String keyAlias;
    @TableField("key_password")
    private String keyPassword;
    private String note;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
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

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
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

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public Integer getEnableSplashSkip() {
        return enableSplashSkip;
    }

    public void setEnableSplashSkip(Integer enableSplashSkip) {
        this.enableSplashSkip = enableSplashSkip;
    }

    public Integer getEnableStartUpVersion() {
        return enableStartUpVersion;
    }

    public void setEnableStartUpVersion(Integer enableStartUpVersion) {
        this.enableStartUpVersion = enableStartUpVersion;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    public String getReserve4() {
        return reserve4;
    }

    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }

    public String getReserve5() {
        return reserve5;
    }

    public void setReserve5(String reserve5) {
        this.reserve5 = reserve5;
    }

    @Override
    public String toString() {
        return "Task{" +
                ", id=" + id +
                ", appId=" + appId +
                ", packageName=" + packageName +
                ", appName=" + appName +
                ", baseUrl=" + baseUrl +
                ", type=" + type +
                ", versionName=" + versionName +
                ", versionCode=" + versionCode +
                ", wxAppId=" + wxAppId +
                ", jpushAppId=" + jpushAppId +
                ", assets=" + assets +
                ", enableSplashSkip=" + enableSplashSkip +
                ", enableStartUpVersion=" + enableStartUpVersion +
                ", status=" + status +
                ", createTime=" + createTime +
                ", downloadUrl=" + downloadUrl +
                ", storePassword=" + storePassword +
                ", keyAlias=" + keyAlias +
                ", keyPassword=" + keyPassword +
                ", note=" + note +
                ", reserve1=" + reserve1 +
                ", reserve2=" + reserve2 +
                ", reserve3=" + reserve3 +
                ", reserve4=" + reserve4 +
                ", reserve5=" + reserve5 +
                "}";
    }

    public String getStatusStr(){
        String statusStr = "";
        if (getStatus() == 1) {
            statusStr = "building";
        } else if (getStatus() == 2) {
            statusStr = "done";
        } else if (getStatus() == 3) {
            statusStr = "failed";
        } else if (getStatus() == 4) {
            statusStr = "canceled";
        } else {
            statusStr = "waiting";
        }
        return statusStr;
    }
}
