package com.hingecloud.apppubs.pub.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hingecloud.apppubs.pub.exception.CreateTaskException;
import com.hingecloud.apppubs.pub.exception.GlobalExceptionHandler;
import com.hingecloud.apppubs.pub.mapper.DicMapper;
import com.hingecloud.apppubs.pub.mapper.TaskMapper;
import com.hingecloud.apppubs.pub.model.TDic;
import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.model.dto.CancelTaskDTO;
import com.hingecloud.apppubs.pub.model.dto.CheckTaskDTO;
import com.hingecloud.apppubs.pub.model.dto.CreateTaskDTO;
import com.hingecloud.apppubs.pub.model.vo.CheckTaskVO;
import com.hingecloud.apppubs.pub.model.vo.CreateTaskVO;
import com.hingecloud.apppubs.pub.service.TaskService;
import com.hingecloud.apppubs.pub.service.impl.compile.CompileHandler;
import com.hingecloud.apppubs.pub.service.impl.compile.CompileHandlerFactory;
import com.hingecloud.apppubs.pub.service.impl.compile.HandlerConfiguration;
import com.hingecloud.apppubs.pub.tools.QiniuHelper;
import com.hingecloud.apppubs.pub.utils.DateUtil;
import com.hingecloud.apppubs.pub.utils.FileHelper;
import com.hingecloud.apppubs.pub.utils.GradleUtils;
import com.hingecloud.apppubs.pub.utils.HttpUtil;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TTask> implements TaskService {

    private final int MAX_TASK_SIZE = 100;

    @Value("${custom.upload.dir}")
    private String uploadDir;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${custom.android.prebuildDir}")
    private String prebuildDir;

    @Value("${custom.android.projectDir}")
    private String androidProjectDir;

    @Value("${custom.android.resultPath}")
    private String androidResultPath;

    @Value("${custom.qiniu.accessKey}")
    private String mAccessKey;

    @Value("${custom.qiniu.secretKey}")
    private String mSecretKey;

    @Value("${custom.qiniu.bucket}")
    private String mBucket;

    @Value("${custom.ios.resultPath}")
    private String mIosResultPath;

    @Value("${custom.ios.projectDir}")
    private String mIosProjectDir;

    @Autowired
    private DicMapper mDicMapper;

    @Value("${custom.qiniu.domain}")
    private String mDomain;

    private ArrayBlockingQueue<TTask> mTaskQueue = new ArrayBlockingQueue(MAX_TASK_SIZE, true);

    @Override
    public CreateTaskVO addTask(CreateTaskDTO dto) throws CreateTaskException {

        if (haveTaskUnFinished(dto.getAppId(), dto.getType())) {
            throw new CreateTaskException("有尚未完成的编译任务！");
        } else {
            String assetsPath = saveAssets(dto.getAssets());

            /*获取最新的版本代码*/

            Integer latestVersionCode = baseMapper.selectLatestVersionCode(dto.getAppId(), dto.getType());
            if (latestVersionCode == null) {
                TDic con = new TDic();
                con.setType(TTask.TYPE_ANDROID.equals(dto.getType()) ? 0 : 1);
                TDic result = mDicMapper.selectOne(con);
                latestVersionCode = Integer.parseInt(result.getValue() + "001");
            } else {
                latestVersionCode++;
            }

            TTask task = new TTask();
            task.setAppId(dto.getAppId());
            task.setPackageName(dto.getPackageName());
            task.setAppName(dto.getAppName());
            task.setBaseUrl(dto.getBaseURL());
            task.setType(dto.getType());
            task.setVersionName(dto.getVersionName());
            task.setWxAppId(dto.getWxAppId());
            task.setJpushAppId(dto.getJpushAppId());
            task.setStorePassword(dto.getStorePassword());
            task.setKeyAlias(dto.getKeyAlias());
            task.setKeyPassword(dto.getKeyPassword());
            task.setAssets(assetsPath);
            task.setVersionCode(latestVersionCode);
            task.setReserve1(dto.getCallback());
            task.setCerPassword(dto.getIosCerPassword());
            task.setBundleId(dto.getIosBundleId());
            baseMapper.add(task);
            if (!mTaskQueue.offer(task)) {
                throw new CreateTaskException("编译队列已满，请稍后再试！");
            } else {
                CreateTaskVO vo = new CreateTaskVO();
                vo.setTaskId(task.getId());
                vo.setQueueNum(mTaskQueue.size());
                vo.setVersionCode(task.getVersionCode());
                vo.setEstimate(mTaskQueue.size() * 120);
                return vo;
            }
        }
    }

    private boolean haveTaskUnFinished(String appId, String type) {
        Wrapper<TTask> wrapper = new EntityWrapper<TTask>();
        wrapper.eq("app_id", appId);
        wrapper.eq("type", type);
        wrapper.le("status", TTask.STATUS_BUILDING);
        List<TTask> listTask = baseMapper.selectList(wrapper);
        return listTask.size() > 0;
    }

    private String saveAssets(MultipartFile file) {
        String filePath = null;
        String dateDir = DateUtil.getDateNow("yyyyMMdd");
        try {
            Path dir = makeSureUploadDirExist(dateDir);
            String filename = buildFilename(file.getOriginalFilename());
            Path desPath = Paths.get(dir.toString(), filename);
            Files.copy(file.getInputStream(), desPath);
            filePath = desPath.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("保存assets文件成功 path: " + filePath);
        return filePath;
    }

    private Path makeSureUploadDirExist(String subDir) throws IOException {
        Path result = Paths.get(uploadDir, subDir);
        Files.createDirectories(result);
        return result;
    }

    @PostConstruct
    private void startTaskResolver() {
        logger.info("打开任务处理器");
        //cleanup android项目
        try {
            GradleUtils.buildProject(androidProjectDir, "clean");
            GradleUtils.buildProject(mIosProjectDir, "clean");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //装载未处理完成的编译任务
        Wrapper<TTask> wrapper = new EntityWrapper<TTask>();
        wrapper.le("status", TTask.STATUS_BUILDING);
        List<TTask> listTask = baseMapper.selectList(wrapper);
        if (listTask.size() > 0) {
            mTaskQueue.addAll(listTask);
        }
        new Thread(new TaskResolver()).start();
    }

    private class TaskResolver implements Runnable {

        @Override
        public void run() {
            while (true) {
                TTask task = null;
                try {
                    task = mTaskQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("正在处理" + task.getAppId());
                task.setStatus(TTask.STATUS_BUILDING);
                changeTaskStatus(task.getId(), TTask.STATUS_BUILDING);

                notifyCallback(task.getReserve1(), task.getId(), task.getStatusStr(), "", "");
                try {
                    HandlerConfiguration conf = new HandlerConfiguration();
                    conf.setAndroidPreBuildDir(prebuildDir);
                    conf.setAndroidProjectDir(androidProjectDir);
                    conf.setAndroidResultPath(androidResultPath);
                    conf.setIosProjectDir(mIosProjectDir);
                    CompileHandler handler = new CompileHandlerFactory(conf).getHandler(task.getType());
                    handler.packageRelease(task);

                    TTask t = baseMapper.selectById(task.getId());
                    if (t.getStatus() != TTask.STATUS_CANCEL) {
                        QiniuHelper qnHelper = new QiniuHelper(mAccessKey, mSecretKey, mBucket);
                        String resultKey = null;
                        if (TTask.TYPE_ANDROID.equals(t.getType())) {
                            resultKey = task.getAppId() + "/v" + task.getVersionName() + "_" + task.getVersionCode() + ".apk";
                            qnHelper.uploadFile(androidResultPath, resultKey);
                        } else {
                            resultKey = task.getAppId() + "/v" + task.getVersionName() + "_" + task.getVersionCode() + ".plist";
                            String ipaKey = task.getAppId() + "/v" + task.getVersionName() + "_" + task.getVersionCode() + ".ipa";
                            String pngPath = mIosProjectDir + "/pub/AppIcon.appiconset/" + "Icon-Small-40@3x.png";
                            String pngKey = task.getAppId() + "/v" + task.getVersionName() + "_" + task.getVersionCode() + ".png";
                            //上传ipa
                            qnHelper.uploadFile(mIosResultPath, ipaKey);
                            //上传png
                            qnHelper.uploadFile(pngPath, pngKey);
                            //制作manifest
                            InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream("ios/manifest_template.plist");

                            String manifestTemplate = FileHelper.convertStreamToString(io);
                            manifestTemplate = manifestTemplate.replace("${app_url}", mDomain + ipaKey)
                                    .replace("${display_image}", mDomain + pngKey)
                                    .replace("${full_size_image}", mDomain + pngKey)
                                    .replace("${bundle_identifer}", task.getBundleId())
                                    .replace("${subtitle}", task.getAppName())
                                    .replace("${title}", task.getAppName());
                            byte[] templateBytes = manifestTemplate.getBytes();
                            Files.copy(new ByteInputStream(templateBytes, templateBytes.length), Paths.get(mIosProjectDir, "pub", "manifest.plist"), StandardCopyOption.REPLACE_EXISTING);
                            qnHelper.uploadFile(Paths.get(mIosProjectDir, "pub", "manifest.plist").toString(), resultKey);
                        }
                        t.setDownloadUrl(mDomain + resultKey);
                        t.setStatus(TTask.STATUS_SUCCESS);
                        baseMapper.updateById(t);
                        notifyCallback(task.getReserve1(), task.getId(), t.getStatusStr(), "", t.getDownloadUrl());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("{}", ExceptionUtils.getMessage(e), e);
                    TTask task1 = baseMapper.selectById(task.getId());
                    task1.setStatus(TTask.STATUS_FAIL);
                    task1.setNote(ExceptionUtils.getMessage(e));
                    baseMapper.updateById(task1);
                    notifyCallback(task.getReserve1(), task.getId(), task1.getStatusStr(), task1.getNote(), "");
                }
            }
        }
    }

    private void changeTaskStatus(int id, int status) {
        TTask task = baseMapper.selectById(id);
        task.setStatus(status);
        baseMapper.updateById(task);
    }

    private void notifyCallback(String callback, Integer taskId, String status, String errMsg, String downloadURL) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskId", taskId + "");
        params.put("errMsg", errMsg);
        params.put("status", status);
        params.put("downloadURL", downloadURL);
        try {
            HttpUtil.sendPost(callback, params);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private String buildFilename(String originalFilename) {
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        return UUID.randomUUID().toString().replaceAll("-", "") + suffix;
    }

    @Override
    public CheckTaskVO checkTask(CheckTaskDTO dto) {
        TTask task = baseMapper.selectById(dto.getTaskId());
        CheckTaskVO vo = new CheckTaskVO();
        vo.setDownloadURL(task.getDownloadUrl());

        vo.setStatus(task.getStatusStr());

        if ("failed".equals(task.getStatusStr())) {
            vo.setErrMsg(task.getNote());
        }
        return vo;
    }

    @Override
    public void cancelTask(CancelTaskDTO dto) {
        for (TTask task : mTaskQueue) {
            if (dto.getTaskId().equals(task.getAppId())) {
                mTaskQueue.remove(task);
                break;
            }
        }
        TTask task = baseMapper.selectById(dto.getTaskId());
        task.setStatus(TTask.STATUS_CANCEL);
        baseMapper.updateById(task);
    }
}
