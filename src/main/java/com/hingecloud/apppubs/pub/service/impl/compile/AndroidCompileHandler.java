package com.hingecloud.apppubs.pub.service.impl.compile;

import com.alibaba.fastjson.JSON;
import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.model.config.AndroidBuildData;
import com.hingecloud.apppubs.pub.utils.FileHelper;
import com.hingecloud.apppubs.pub.utils.GradleUtils;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class AndroidCompileHandler implements CompileHandler {

    private String mPrebuildDir;

    private String mAndroidProjectDir;

    public AndroidCompileHandler(String prebuildDir, String projectDir) {
        mPrebuildDir = prebuildDir;
        mAndroidProjectDir = projectDir;
    }

    @Override
    public void packageRelease(TTask task) throws IOException {
        cleanInputDir();
        moveBuildFile();
        moveAssets(task.getAssets());
        createConfigFile(task);
        createIcons();
        GradleUtils.buildProject(mPrebuildDir, "resolveSource");
        GradleUtils.buildProject(mAndroidProjectDir, "packageRelease");
    }

    private void cleanInputDir() {
        FileHelper.delFolder(Paths.get(mPrebuildDir, "input").toString());
    }

    private void moveBuildFile() throws IOException {
        InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream("android/build.gradle");
        Files.copy(io, Paths.get(mPrebuildDir, "build.gradle"), StandardCopyOption.REPLACE_EXISTING);
    }

    private void moveAssets(String assetsPath) throws IOException {
        FileHelper.decompressZip(new File(assetsPath), Paths.get(mPrebuildDir, "input").toString());
    }

    private void createIcons() throws IOException {
        createIcon(192, "xxxhdpi");
        createIcon(144, "xxhdpi");
        createIcon(96, "xhdpi");
        createIcon(72, "hdpi");
        createIcon(48, "mdpi");
        createIcon(36, "ldpi");
    }

    private void createIcon(int lengthOfSlide, String dir) throws IOException {
        File mataIcon = Paths.get(mPrebuildDir, "input", "icon.png").toFile();
        File desDir = Paths.get(mPrebuildDir, "input", "icons", dir).toFile();
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        Thumbnails.of(mataIcon)
                .size(lengthOfSlide, lengthOfSlide)
                .allowOverwrite(true)
                .outputFormat("png").toFile(Paths.get(desDir.getAbsolutePath(), "icon.png").toFile());
    }

    private void createConfigFile(TTask task) throws IOException {
        AndroidBuildData data = AndroidBuildData.createFromTask(task);
        String jsonStr = JSON.toJSONString(data);
        ByteInputStream bis = new ByteInputStream(jsonStr.getBytes(), 0, jsonStr.getBytes().length);
        Files.copy(bis, Paths.get(mPrebuildDir, "input", "config.json"));
    }

}
