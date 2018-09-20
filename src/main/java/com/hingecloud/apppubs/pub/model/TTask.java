package com.hingecloud.apppubs.pub.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
/**
 * <p>
 *  实体类
 * </p>
 *
 * @author 张稳
 * @since 2018-09-20 
 */
@TableName("task")
public class TTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    @TableField("appId")
    private String appId;
    @TableField("packageName")
    private String packageName;
    @TableField("appName")
    private String appName;
    @TableField("baseURL")
    private String baseURL;
    private String type;
    @TableField("versionName")
    private String versionName;
    @TableField("wxAppId")
    private String wxAppId;
    @TableField("jpushAppId")
    private String jpushAppId;
    private String assets;
    @TableField("enableSplashSkip")
    private Integer enableSplashSkip;
    @TableField("enableStartUpVersion")
    private Integer enableStartUpVersion;
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
            "id=" + id +
            ", appId=" + appId +
            ", packageName=" + packageName +
            ", appName=" + appName +
            ", baseURL=" + baseURL +
            ", type=" + type +
            ", versionName=" + versionName +
            ", wxAppId=" + wxAppId +
            ", jpushAppId=" + jpushAppId +
            ", assets=" + assets +
            ", enableSplashSkip=" + enableSplashSkip +
            ", enableStartUpVersion=" + enableStartUpVersion +
            ", reserve1=" + reserve1 +
            ", reserve2=" + reserve2 +
            ", reserve3=" + reserve3 +
            ", reserve4=" + reserve4 +
            ", reserve5=" + reserve5 +
            "}";
    }
}
