package com.hingecloud.apppubs.pub.service.impl.compile;

import com.alibaba.fastjson.JSON;
import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.model.config.IOSBuildData;
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
import java.util.ArrayList;
import java.util.List;

public class IOSCompilerHandler implements CompileHandler {

    private String mProjectDir;

    public IOSCompilerHandler(String projectDir) {
        mProjectDir = projectDir;
    }

    @Override
    public void packageRelease(TTask task) throws IOException {
        cleanPubDir();
        decompressAssets(task.getAssets());
        createConfigFile(task);
        GradleUtils.buildProject(mProjectDir, "packageRelease");
    }

    private void cleanPubDir() {
        FileHelper.delFolder(Paths.get(mProjectDir, "pub").toString());
        Paths.get(mProjectDir, "pub/welcome").toFile().mkdirs();
    }

    private void createConfigFile(TTask task) throws IOException {
        IOSBuildData data = IOSBuildData.createFromTask(task);
        String jsonStr = JSON.toJSONString(data);
        ByteInputStream bis = new ByteInputStream(jsonStr.getBytes(), 0, jsonStr.getBytes().length);
        Files.copy(bis, Paths.get(mProjectDir, "pub", "config.json"), StandardCopyOption.REPLACE_EXISTING);
    }

    private void decompressAssets(String assetsPath) throws IOException {
        FileHelper.decompressZip(new File(assetsPath), Paths.get(mProjectDir, "pub").toString());
        copySignFiles();
        copyIcon();
    }

    private void copyIcon() throws IOException {
        AppIconContents content = new AppIconContents();
        content.addImage(createPNG(1024, "ios-marketing", "Icon-marketing.png", 1));
        content.addImage(createPNG(20, "iphone", "Icon-Notification@2x.png", 2));
        content.addImage(createPNG(20, "iphone", "Icon-Notification@3x.png", 3));
        content.addImage(createPNG(29, "iphone", "Icon-Small@2x.png", 2));
        content.addImage(createPNG(29, "iphone", "Icon-Small@3x.png", 3));
        content.addImage(createPNG(40, "iphone", "Icon-Small-40@2x.png", 2));
        content.addImage(createPNG(40, "iphone", "Icon-Small-40@3x.png", 3));
        content.addImage(createPNG(60, "iphone", "Icon-60@2x.png", 2));
        content.addImage(createPNG(60, "iphone", "Icon-60@3x.png", 3));
        content.addImage(createPNG(20, "ipad", "Icon-Notification.png", 1));
        content.addImage(createPNG(20, "ipad", "Icon-Notification@2x.png", 2));
        content.addImage(createPNG(29, "ipad", "Icon-Small.png", 1));
        content.addImage(createPNG(29, "ipad", "Icon-Small@2x.png", 2));
        content.addImage(createPNG(40, "ipad", "Icon-Small-40.png", 1));
        content.addImage(createPNG(40, "ipad", "Icon-Small-40@2x.png", 2));
        content.addImage(createPNG(76, "ipad", "Icon-76.png", 1));
        content.addImage(createPNG(76, "ipad", "Icon-76@2x.png", 2));
        content.addImage(createPNG(83.5f, "ipad", "Icon-83.5@2x.png", 2));
        String jsonStr = JSON.toJSONString(content);
        ByteInputStream bis = new ByteInputStream(jsonStr.getBytes(), 0, jsonStr.getBytes().length);
        Files.copy(bis, Paths.get(mProjectDir, "pub", "AppIcon.appiconset", "Contents.json"));
    }

    private AppIconContentsItem createPNG(float sideLength, String idiom, String filename, int scale) throws IOException {
        File originalFile = Paths.get(mProjectDir, "pub", "icon.png").toFile();
        File appIconDir = Paths.get(mProjectDir, "pub", "AppIcon.appiconset").toFile();
        if (!appIconDir.exists()) {
            appIconDir.mkdirs();
        }
        int realLength = (int) (sideLength*scale);
        Thumbnails.of(originalFile)
                .size(realLength, realLength)
                .outputFormat("png").toFile(Paths.get(mProjectDir, "pub", "AppIcon.appiconset", filename).toFile());
        AppIconContentsItem item = new AppIconContentsItem();
        item.setFilename(filename);
        item.setIdiom(idiom);
        item.setScale(String.format("%dx", scale));
        item.setSize(String.format("%d*%d", realLength, realLength));
        return item;
    }

    //从mobileprovision和cer文件，如果不存在则复制默认的描述文件和证书
    private void copySignFiles() throws IOException {
        File[] fileList = Paths.get(mProjectDir, "pub").toFile().listFiles();
        boolean didProvisionContained = false;
        for (File file : fileList) {
            if ("mp.mobileprovision".equals(file.getName()) || "cer.p12".equals(file.getName())) {
                didProvisionContained = true;
            }
        }
        if (!didProvisionContained) {
            InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream("ios/mp.mobileprovision");
            Files.copy(io, Paths.get(mProjectDir, "pub/mp.mobileprovision"));
            InputStream cerIO = Thread.currentThread().getContextClassLoader().getResourceAsStream("ios/cer.p12");
            Files.copy(cerIO, Paths.get(mProjectDir, "pub/cer.p12"));
        }
    }

    class AppIconContents {
        private List<AppIconContentsItem> images;

        public List<AppIconContentsItem> getImages() {
            return images;
        }

        public void addImage(AppIconContentsItem item) {
            if (images == null) {
                images = new ArrayList();
            }
            images.add(item);
        }
    }


    class AppIconContentsItem {
        private String size;
        private String idiom;
        private String filename;
        private String scale;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getIdiom() {
            return idiom;
        }

        public void setIdiom(String idiom) {
            this.idiom = idiom;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getScale() {
            return scale;
        }

        public void setScale(String scale) {
            this.scale = scale;
        }
    }
}
