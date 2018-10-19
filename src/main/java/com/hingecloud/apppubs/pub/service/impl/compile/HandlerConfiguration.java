package com.hingecloud.apppubs.pub.service.impl.compile;

public class HandlerConfiguration {
    private String androidPreBuildDir;
    private String androidProjectDir;
    private String androidResultPath;
    private String iosProjectDir;
    private String iosResultPath;

    public String getAndroidPreBuildDir() {
        return androidPreBuildDir;
    }

    public void setAndroidPreBuildDir(String androidPreBuildDir) {
        this.androidPreBuildDir = androidPreBuildDir;
    }

    public String getAndroidProjectDir() {
        return androidProjectDir;
    }

    public void setAndroidProjectDir(String androidProjectDir) {
        this.androidProjectDir = androidProjectDir;
    }

    public String getAndroidResultPath() {
        return androidResultPath;
    }

    public void setAndroidResultPath(String androidResultPath) {
        this.androidResultPath = androidResultPath;
    }

    public String getIosProjectDir() {
        return iosProjectDir;
    }

    public void setIosProjectDir(String iosProjectDir) {
        this.iosProjectDir = iosProjectDir;
    }

    public String getIosResultPath() {
        return iosResultPath;
    }

    public void setIosResultPath(String iosResultPath) {
        this.iosResultPath = iosResultPath;
    }
}
