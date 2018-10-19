package com.hingecloud.apppubs.pub.service.impl.compile;

import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.utils.StringUtil;

public class CompileHandlerFactory {

    private HandlerConfiguration mConfig;

    public CompileHandlerFactory(HandlerConfiguration config) {
        mConfig = config;
    }

    public CompileHandler getHandler(String taskType) {
        if (StringUtil.isEmpty(taskType)) {
            throw new IllegalArgumentException("taskType 不可为空");
        } else {
            if (!taskType.equals(TTask.TYPE_ANDROID) && !taskType.equals(TTask.TYPE_IOS)) {
                throw new IllegalArgumentException("非法的类型！");
            }
        }
        if (taskType.equals(TTask.TYPE_ANDROID)) {
            return new AndroidCompileHandler(mConfig.getAndroidPreBuildDir(), mConfig.getAndroidProjectDir());
        } else {
            return new IOSCompilerHandler(mConfig.getIosProjectDir());
        }
    }
}
