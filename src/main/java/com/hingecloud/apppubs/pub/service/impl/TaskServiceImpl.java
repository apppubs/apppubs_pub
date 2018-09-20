package com.hingecloud.apppubs.pub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.hingecloud.apppubs.pub.exception.GlobalExceptionHandler;
import com.hingecloud.apppubs.pub.mapper.TaskMapper;
import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.model.dto.CreateTaskDTO;
import com.hingecloud.apppubs.pub.service.TaskService;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TTask> implements TaskService {

    private final int MAX_TASK_SIZE = 100;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${custom.android.prebuildDir}")
    private String prebuildDir;

    @Value("${custom.android.projectDir}")
    private String androidProjectDir;

    @Value("${custom.qiniu.accessKey}")
    private String mAccessKey;

    @Value("${custom.qiniu.secretKey}")
    private String mSecretKey;

    @Value("${custom.qiniu.bucket}")
    private String mBucket;
    private ArrayBlockingQueue mTaskQueue = new ArrayBlockingQueue(MAX_TASK_SIZE, true);

    public boolean addTask(CreateTaskDTO dto) {
        mTaskQueue.add("fdfd");
        return true;
    }

    public boolean buildProject(String buildPath, String... tasks) {
        File buildFile = new File(buildPath);
        GradleConnector connector = GradleConnector.newConnector();
        connector.forProjectDirectory(buildFile);
        ProjectConnection connection = connector.connect();
        BuildLauncher build = connection.newBuild();
        build.forTasks(tasks);
        try {
            build.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return true;
    }

    private void uploadFile(String localFilePath, String key) {
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String upToken = getAccessToken();
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            throw new RuntimeException(r.toString());
        }
    }

    private String getAccessToken() {
        Auth auth = Auth.create(mAccessKey, mSecretKey);
        String upToken = auth.uploadToken(mBucket);
        return upToken;
    }

}
