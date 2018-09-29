package com.hingecloud.apppubs.pub.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.hingecloud.apppubs.pub.exception.CreateTaskException;
import com.hingecloud.apppubs.pub.exception.GlobalExceptionHandler;
import com.hingecloud.apppubs.pub.mapper.DicMapper;
import com.hingecloud.apppubs.pub.mapper.TaskMapper;
import com.hingecloud.apppubs.pub.model.TDic;
import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.model.config.BuildData;
import com.hingecloud.apppubs.pub.model.dto.CancelTaskDTO;
import com.hingecloud.apppubs.pub.model.dto.CheckTaskDTO;
import com.hingecloud.apppubs.pub.model.dto.CreateTaskDTO;
import com.hingecloud.apppubs.pub.model.vo.CheckTaskVO;
import com.hingecloud.apppubs.pub.model.vo.CreateTaskVO;
import com.hingecloud.apppubs.pub.service.TaskService;
import com.hingecloud.apppubs.pub.utils.DateUtil;
import com.hingecloud.apppubs.pub.utils.FileHelper;
import com.hingecloud.apppubs.pub.utils.HttpUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    @Autowired
    private DicMapper mDicMapper;

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
            buildProject(androidProjectDir, "clean");
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
                    cleanInputDir();
                    moveBuildFile();
                    moveAssets(task.getAssets());
                    createConfigFile(task);
                    buildProject(prebuildDir, "resolveSource");
                    buildProject(androidProjectDir, "packageRelease");

                    TTask t = baseMapper.selectById(task.getId());
                    if (t.getStatus() != TTask.STATUS_CANCEL) {
                        String apkPath = task.getAppId() + "/v" + task.getVersionName() + "_" + task.getVersionCode() + ".apk";
                        uploadFile(androidResultPath, apkPath);
                        t.setDownloadUrl("http://qiniu.apppubs.com/" + apkPath);
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

        private void cleanInputDir() {
            FileHelper.delFolder(Paths.get(prebuildDir, "input").toString());
        }

        private void moveBuildFile() throws IOException {
            InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream("android/build.gradle");
            Files.copy(io, Paths.get(prebuildDir, "build.gradle"), StandardCopyOption.REPLACE_EXISTING);
        }

        private void createConfigFile(TTask task) throws IOException {
            BuildData data = BuildData.createFromTask(task);
            String jsonStr = JSON.toJSONString(data);
            ByteInputStream bis = new ByteInputStream(jsonStr.getBytes(), 0, jsonStr.getBytes().length);
            Files.copy(bis, Paths.get(prebuildDir, "input", "config.json"));
        }

        private void moveAssets(String assetsPath) throws IOException {
            FileHelper.decompressZip(new File(assetsPath), Paths.get(prebuildDir, "input").toString());
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

    public boolean buildProject(String buildPath, String... tasks) throws Exception {
        File buildFile = new File(buildPath);
        GradleConnector connector = GradleConnector.newConnector();
        connector.forProjectDirectory(buildFile);
        ProjectConnection connection = connector.connect();
        BuildLauncher build = connection.newBuild();
        build.forTasks(tasks);
        build.setStandardOutput(System.out);
        try {
            build.run();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            connection.close();
        }
        return true;
    }

    private void uploadFile(String localFilePath, String key) throws QiniuException {
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String upToken = getAccessToken();
        Response response = uploadManager.put(localFilePath, key, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        System.out.println(putRet.key);
        System.out.println(putRet.hash);
    }

    private String getAccessToken() {
        Auth auth = Auth.create(mAccessKey, mSecretKey);
        String upToken = auth.uploadToken(mBucket);
        return upToken;
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
